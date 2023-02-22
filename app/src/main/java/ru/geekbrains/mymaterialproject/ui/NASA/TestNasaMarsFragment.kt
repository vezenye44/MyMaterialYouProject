package ru.geekbrains.mymaterialproject.ui.NASA

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.geekbrains.mymaterialproject.databinding.FragmentTestNasaMarsBinding


class TestNasaMarsFragment : Fragment() {

    private var _binding: FragmentTestNasaMarsBinding? = null
    private val binding get() = _binding!!
    private var isExpanded = false
    private val duration = 1000L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTestNasaMarsBinding.inflate(inflater, container, false)

        binding.fab.setOnClickListener {
            isExpanded = !isExpanded
            if (isExpanded) expandFAB()
            else collapseFAB()
        }

        return binding.root
    }

    private fun expandFAB() {
        ObjectAnimator.ofFloat(binding.transparentBackground, View.ALPHA, 0.5f)
            .setDuration(duration)
            .start()

        ObjectAnimator.ofFloat(binding.plusImageView, View.ROTATION, 0f, 225f)
            .setDuration(duration)
            .start()

        ObjectAnimator.ofFloat(binding.optionOneContainer, View.TRANSLATION_Y, -130f)
            .setDuration(duration)
            .start()
        ObjectAnimator.ofFloat(binding.optionOneContainer, View.ALPHA, 1f)
            .setDuration(duration)
            .start()

        ObjectAnimator.ofFloat(binding.optionTwoContainer, View.TRANSLATION_Y, -250f)
            .setDuration(duration)
            .start()
        ObjectAnimator.ofFloat(binding.optionTwoContainer, View.ALPHA, 1f)
            .setDuration(duration)
            .start()

    }

    private fun collapseFAB() {
        with(binding) {
            plusImageView.animate()
                .rotation(0f)

            transparentBackground.animate()
                .alpha(0f)
                .duration = duration

            optionTwoContainer.animate()
                .alpha(0f)
                .translationY(0f)
                .duration = duration

            optionOneContainer.animate()
                .alpha(0f)
                .translationY(0f)
                .duration = duration

        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = TestNasaMarsFragment()
    }
}