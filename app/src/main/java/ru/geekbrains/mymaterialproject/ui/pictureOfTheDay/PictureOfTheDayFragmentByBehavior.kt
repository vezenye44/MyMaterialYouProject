package ru.geekbrains.mymaterialproject.ui.pictureOfTheDay

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ru.geekbrains.mymaterialproject.R
import ru.geekbrains.mymaterialproject.data.PictureOfTheDayDTO
import ru.geekbrains.mymaterialproject.data.PictureOfTheDayData
import ru.geekbrains.mymaterialproject.databinding.FragmentPictureOfTheDayByBehaviorBinding
import ru.geekbrains.mymaterialproject.ui.MainActivity
import ru.geekbrains.mymaterialproject.viewmodel.pictureOfTheDay.PictureOfTheDayViewModel

class PictureOfTheDayFragmentByBehavior : Fragment() {

    private var _binding: FragmentPictureOfTheDayByBehaviorBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var selectedChipName: String? = null

    //Ленивая инициализация модели
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getData().observe(viewLifecycleOwner) { renderData(it) }
        _binding = FragmentPictureOfTheDayByBehaviorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.scrollView.setOnScrollChangeListener { _, _, _, _, _ ->
                binding.appbar.isSelected = binding.scrollView.canScrollVertically(-1)
            }
        }

        restoringChipState()
        setListenersForChips()
    }

    private fun restoringChipState() {
        requireActivity().getSharedPreferences(MainActivity.KEY_SP, Activity.MODE_PRIVATE)
            .getString(EXTRA_CHIP_SELECTED, "Empty")
            .also { chipName ->
                when (chipName) {
                    Chips.ChipToday.name -> {
                        binding.chipToday.isChecked = true
                        viewModel.todayPictureOfTheDay()
                    }
                    Chips.ChipYesterday.name -> {
                        binding.chipYesterday.isChecked = true
                        viewModel.yesterdayPictureOfTheDay()
                    }
                    Chips.ChipBeforeYesterday.name -> {
                        binding.chipBeforeYesterday.isChecked = true
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
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

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                //val url = serverResponseData.url
                if (serverResponseData.url.isNullOrEmpty()) {
                    showError("Link is empty")
                } else if (serverResponseData.mediaType == "image") {
                    loadImage(serverResponseData)
                } else {
                    loadVideo(serverResponseData)
                }
            }
            is PictureOfTheDayData.Loading -> {
                //binding.playVideoBtn.isGone = true
                binding.imageView.load(R.drawable.ic_image_loading)
                binding.bottomSheetDescriptionHeader.text = ""
                binding.bottomSheetDescription.text = ""
            }
            is PictureOfTheDayData.Error -> {
                //binding.playVideoBtn.isGone = true
                showError(data.error.message)
                binding.bottomSheetDescriptionHeader.text = ""
                binding.bottomSheetDescription.text = ""
            }
            else -> {}
        }
    }

    private fun loadVideo(serverResponseData: PictureOfTheDayDTO) {
        binding.imageView.load(R.drawable.preview)
        /*binding.playVideoBtn.apply {
            isGone = false
            setOnClickListener {
                *//*startActivity(Intent(Intent.ACTION_VIEW).apply {
                    this.data = Uri.parse(serverResponseData.url)
                })*//*
                this@PictureOfTheDayFragmentByBehavior.requireActivity().supportFragmentManager.beginTransaction()
                    .hide(this@PictureOfTheDayFragmentByBehavior)
                    .add(VideoPlayerFragment.newInstance(serverResponseData.url), "")
                    .addToBackStack("")
                    .commit()
            }
        }*/
        binding.bottomSheetDescriptionHeader.text = serverResponseData.title
        binding.bottomSheetDescription.text = serverResponseData.explanation
    }

    private fun loadImage(serverResponseData: PictureOfTheDayDTO) {
        //binding.playVideoBtn.isGone = true
        binding.imageView.load(serverResponseData.url) {
            lifecycle(this@PictureOfTheDayFragmentByBehavior)
            error(R.drawable.ic_load_error_vector)
            placeholder(R.drawable.ic_no_photo_vector)
            crossfade(true)
        }
        binding.bottomSheetDescriptionHeader.text = serverResponseData.title
        binding.bottomSheetDescription.text = serverResponseData.explanation
    }

    private fun Fragment.showError(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EXTRA_CHIP_SELECTED, selectedChipName)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PictureOfTheDayFragmentByBehavior()
        const val EXTRA_CHIP_SELECTED = "EXTRA_CHIP_SELECTED"
    }

    sealed class Chips(val name: String) {
        object ChipToday : Chips("CHIP_TODAY")
        object ChipYesterday : Chips("CHIP_YESTERDAY")
        object ChipBeforeYesterday : Chips("CHIP_BEFORE_YESTERDAY")
    }
}