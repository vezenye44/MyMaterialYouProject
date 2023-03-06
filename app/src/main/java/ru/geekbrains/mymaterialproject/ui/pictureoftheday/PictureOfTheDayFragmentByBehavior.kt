package ru.geekbrains.mymaterialproject.ui.pictureoftheday

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BulletSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import ru.geekbrains.mymaterialproject.R
import ru.geekbrains.mymaterialproject.data.PictureOfTheDayDTO
import ru.geekbrains.mymaterialproject.data.PictureOfTheDayData
import ru.geekbrains.mymaterialproject.databinding.FragmentPictureOfTheDayByBehaviorBinding
import ru.geekbrains.mymaterialproject.ui.MainActivity

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
                        updateSpannableInChipGroup(CHIP_TODAY_POSITION)
                        binding.chipToday.isChecked = true
                        viewModel.todayPictureOfTheDay()
                    }
                    Chips.ChipYesterday.name -> {
                        updateSpannableInChipGroup(CHIP_YESTERDAY_POSITION)
                        binding.chipYesterday.isChecked = true
                        viewModel.yesterdayPictureOfTheDay()
                    }
                    Chips.ChipBeforeYesterday.name -> {
                        updateSpannableInChipGroup(CHIP_BEFORE_YESTERDAY_POSITION)
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
                updateSpannableInChipGroup(CHIP_TODAY_POSITION)
                viewModel.todayPictureOfTheDay()
                saveChipSelectedPosition(Chips.ChipToday)
            }
            chipYesterday.setOnClickListener {
                updateSpannableInChipGroup(CHIP_YESTERDAY_POSITION)
                viewModel.yesterdayPictureOfTheDay()
                saveChipSelectedPosition(Chips.ChipYesterday)
            }
            chipBeforeYesterday.setOnClickListener {
                updateSpannableInChipGroup(CHIP_BEFORE_YESTERDAY_POSITION)
                viewModel.beforeYesterdayPictureOfTheDay()
                saveChipSelectedPosition(Chips.ChipBeforeYesterday)
            }
        }
    }


    private val spannableStringToday by lazy {SpannableString(binding.chipToday.text)}
    private val spannableStringYesterday by lazy {SpannableString(binding.chipYesterday.text)}
    private val spannableStringBeforeYesterday by lazy {SpannableString(binding.chipBeforeYesterday.text)}
    private val spannableStringList by lazy { listOf(
        spannableStringToday,
        spannableStringYesterday,
        spannableStringBeforeYesterday
    )}

    private fun updateSpannableInChipGroup(position: Int) {
        val colorBlue = ContextCompat.getColor(requireContext(), android.R.color.holo_blue_light)
        val colorRed = ContextCompat.getColor(requireContext(), android.R.color.holo_red_light)
        val bulletSpanBlue = BulletSpan(20, colorBlue)
        val bulletSpanRed = BulletSpan(20, colorRed)

        for (i in 0..2) {
            val chip = (binding.chipGroup[i] as Chip)
            if (i == position) {
                val spannableString = spannableStringList[i]
                val spans = spannableString.getSpans(0, spannableString.length, BulletSpan::class.java)
                for (span in spans) spannableString.removeSpan(span)
                spannableString.setSpan(
                    bulletSpanBlue,
                    0,
                    spannableString.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                chip.text = spannableString
            } else {
                val spannableString = spannableStringList[i]
                val spans = spannableString.getSpans(0, spannableString.length, BulletSpan::class.java)
                for (span in spans) spannableString.removeSpan(span)
                spannableString.setSpan(
                    bulletSpanRed,
                    0,
                    spannableString.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                chip.text = spannableString
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
                if (serverResponseData.url.isNullOrEmpty()) {
                    showError("Link is empty")
                } else if (serverResponseData.mediaType == "image") {
                    loadImage(serverResponseData)
                } else {
                    loadVideo(serverResponseData)
                }
            }
            is PictureOfTheDayData.Loading -> {
                binding.imageView.load(R.drawable.ic_image_loading)
                binding.bottomSheetDescriptionHeader.text = ""
                binding.bottomSheetDescription.text = ""
            }
            is PictureOfTheDayData.Error -> {
                showError(data.error.message)
                binding.bottomSheetDescriptionHeader.text = ""
                binding.bottomSheetDescription.text = ""
            }
            else -> {}
        }
    }

    private fun loadVideo(serverResponseData: PictureOfTheDayDTO) {
        binding.imageView.load(R.drawable.preview)
        binding.bottomSheetDescriptionHeader.text = serverResponseData.title
        binding.bottomSheetDescription.text = serverResponseData.explanation
    }

    private fun loadImage(serverResponseData: PictureOfTheDayDTO) {
        binding.imageView.load(serverResponseData.url) {
            lifecycle(this@PictureOfTheDayFragmentByBehavior)
            error(R.drawable.ic_load_error_vector)
            placeholder(R.drawable.ic_no_photo_vector)
            crossfade(true)
        }

        val spannableString: SpannableString = customizeDescriptionTitleText(serverResponseData)
        binding.bottomSheetDescriptionHeader.text = spannableString

        binding.bottomSheetDescription.text = serverResponseData.explanation
    }

    private fun customizeDescriptionTitleText(serverResponseData: PictureOfTheDayDTO): SpannableString {
        val spannableString: SpannableString = SpannableString(serverResponseData.title)
        val color = ContextCompat.getColor(requireContext(), android.R.color.holo_blue_light)
        spannableString.setSpan(
            ForegroundColorSpan(color),
            0,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val fontSizeMultiplier = 1.2f
        spannableString.setSpan(
            RelativeSizeSpan(fontSizeMultiplier),
            0,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
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
        const val CHIP_TODAY_POSITION = 0
        const val CHIP_YESTERDAY_POSITION = 1
        const val CHIP_BEFORE_YESTERDAY_POSITION = 2

        fun newInstance() = PictureOfTheDayFragmentByBehavior()
        const val EXTRA_CHIP_SELECTED = "EXTRA_CHIP_SELECTED"
    }

    sealed class Chips(val name: String) {
        object ChipToday : Chips("CHIP_TODAY")
        object ChipYesterday : Chips("CHIP_YESTERDAY")
        object ChipBeforeYesterday : Chips("CHIP_BEFORE_YESTERDAY")
    }
}