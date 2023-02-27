package ru.geekbrains.mymaterialproject.ui.NASA

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import ru.geekbrains.mymaterialproject.R
import ru.geekbrains.mymaterialproject.databinding.FragmentTestNasaSystemBinding


class TestNasaSystemFragment : Fragment() {

    private lateinit var binding: FragmentTestNasaSystemBinding
    private var show = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTestNasaSystemBinding.inflate(layoutInflater, container, false)

        binding.backgroundImage.setOnClickListener { if (show) hideComponents() else
            showComponents() }

        return binding.root
    }

    private fun showComponents() {
        show = true
        val constraintSet = ConstraintSet()
        constraintSet.clone(requireContext(), R.layout.fragment_test_nasa_system_second_state)
        val transition = ChangeBounds()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        }
        transition.duration = 1200
        TransitionManager.beginDelayedTransition(binding.root,
            transition)
        constraintSet.applyTo(binding.root)
    }

    private fun hideComponents() {
        show = false
        val constraintSet = ConstraintSet()
        constraintSet.clone(requireContext(), R.layout.fragment_test_nasa_system)
        val transition = ChangeBounds()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        }
        transition.duration = 1200
        TransitionManager.beginDelayedTransition(binding.root,
            transition)
        constraintSet.applyTo(binding.root)
    }

    companion object {
        @JvmStatic
        fun newInstance() = TestNasaSystemFragment()
    }
}