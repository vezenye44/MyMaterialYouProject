package ru.geekbrains.mymaterialproject.ui.pictureOfTheDay

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ru.geekbrains.mymaterialproject.R
import ru.geekbrains.mymaterialproject.data.PictureOfTheDayData
import ru.geekbrains.mymaterialproject.databinding.FragmentPictureOfTheDayBinding
import ru.geekbrains.mymaterialproject.ui.MainActivity
import ru.geekbrains.mymaterialproject.ui.settings.SettingsFragment
import ru.geekbrains.mymaterialproject.viewmodel.pictureOfTheDay.PictureOfTheDayViewModel

class PictureOfTheDayFragment : Fragment() {

    private var _binding: FragmentPictureOfTheDayBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    //Ленивая инициализация модели
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getData().observe(viewLifecycleOwner) { renderData(it) }
        _binding = FragmentPictureOfTheDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }

        if (savedInstanceState == null) {
            binding.chipToday.isChecked = true
            viewModel.todayPictureOfTheDay()
        } else {
            requireActivity().getSharedPreferences(MainActivity.KEY_SP, Activity.MODE_PRIVATE)
                .getString(EXTRA_CHIP_SELECTED, "Empty")
                .also { chipName ->
                    when (chipName) {
                        Chips.ChipToday.name -> {
                            viewModel.todayPictureOfTheDay()
                        }
                        Chips.ChipYesterday.name -> {
                            viewModel.yesterdayPictureOfTheDay()
                        }
                        Chips.ChipBeforeYesterday.name -> {
                            viewModel.beforeYesterdayPictureOfTheDay()
                        }
                        "Empty" -> {
                            binding.chipToday.isChecked = true
                            viewModel.todayPictureOfTheDay()
                        }
                        else -> Throwable("Error in chip name selected | chipName = $chipName")
                    }
                }
        }

        setListenersForChips()
        setBottomAppBar(view)
        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_search -> {
                requireActivity().supportFragmentManager.beginTransaction().hide(this)
                    .add(R.id.container, SettingsFragment.newInstance(), "").addToBackStack("").commit()
            }
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, "tag")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun setListenersForChips() {
        with(binding) {
            chipToday.setOnClickListener {
                viewModel.todayPictureOfTheDay()
                saveChipSelectedPosition(Chips.ChipToday)
            }
            chipYesterday.setOnClickListener {
                viewModel.yesterdayPictureOfTheDay()
                saveChipSelectedPosition(Chips.ChipYesterday)
            }
            chipBeforeYesterday.setOnClickListener {
                viewModel.beforeYesterdayPictureOfTheDay()
                saveChipSelectedPosition(Chips.ChipBeforeYesterday)
            }
        }
    }

    private fun saveChipSelectedPosition(chipName: Chips) {
        requireActivity().getSharedPreferences(MainActivity.KEY_SP, Activity.MODE_PRIVATE)
            .edit()
            .putString(EXTRA_CHIP_SELECTED, chipName.name)
            .apply()
    }

    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
        setHasOptionsMenu(true)
        binding.fab.setOnClickListener {
            if (isMain) {
                isMain = false
                binding.bottomAppBar.navigationIcon = null
                binding.bottomAppBar.fabAlignmentMode =
                    BottomAppBar.FAB_ALIGNMENT_MODE_END
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_back_fab
                    )
                )
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
            } else {
                isMain = true
                binding.bottomAppBar.navigationIcon =
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_hamburger_menu_bottom_bar
                    )
                binding.bottomAppBar.fabAlignmentMode =
                    BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_plus_fab
                    )
                )
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
            }
        }

    }

    private fun renderData(data: PictureOfTheDayData) {
        val bottomSheetDescriptionHeaderView: TextView? =
            requireActivity().findViewById<TextView>(R.id.bottomSheetDescriptionHeader)
        val bottomSheetDescriptionView: TextView? =
            requireActivity().findViewById<TextView>(R.id.bottomSheetDescription)

        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    showError("Link is empty")
                } else {
                    binding.imageView.load(url) {
                        lifecycle(this@PictureOfTheDayFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                        crossfade(true)
                    }
                    bottomSheetDescriptionHeaderView?.text = serverResponseData.title
                    bottomSheetDescriptionView?.text = serverResponseData.explanation
                }
            }
            is PictureOfTheDayData.Loading -> {
                binding.imageView.load(R.drawable.ic_image_loading)
                bottomSheetDescriptionHeaderView?.text = ""
                bottomSheetDescriptionView?.text = ""
            }
            is PictureOfTheDayData.Error -> {
                showError(data.error.message)
                bottomSheetDescriptionHeaderView?.text = ""
                bottomSheetDescriptionView?.text = ""
            }
            else -> {}
        }
    }

    private fun Fragment.showError(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
        private var isMain = true
        const val EXTRA_CHIP_SELECTED = "EXTRA_CHIP_SELECTED"
    }

    sealed class Chips(val name: String) {
        object ChipToday : Chips("CHIP_TODAY")
        object ChipYesterday : Chips("CHIP_YESTERDAY")
        object ChipBeforeYesterday : Chips("CHIP_BEFORE_YESTERDAY")
    }
}