package ru.geekbrains.mymaterialproject.ui.NASA

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import ru.geekbrains.mymaterialproject.databinding.FragmentTestNasaEarthBinding


class TestNasaEarthFragment : Fragment() {

    private var _binding: FragmentTestNasaEarthBinding? = null
    private val binding get() = _binding!!
    private var isExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTestNasaEarthBinding.inflate(inflater, container, false)

        binding.imageView.setOnClickListener() {imageView ->
            isExpanded = !isExpanded
            TransitionManager.beginDelayedTransition(binding.root, TransitionSet().apply {
                duration = 2000
                addTransition(ChangeBounds())
                addTransition(ChangeImageTransform())
            })

            (imageView as ImageView).scaleType =
                if (isExpanded) ImageView.ScaleType.CENTER_CROP
                else ImageView.ScaleType.FIT_CENTER

            /*with(imageView as ImageView) {
                *//*layoutParams.height =
                    if (isExpanded) ViewGroup.LayoutParams.MATCH_PARENT
                    else ViewGroup.LayoutParams.MATCH_PARENT*//*

                scaleType =
                    if (isExpanded) ImageView.ScaleType.CENTER_CROP
                    else ImageView.ScaleType.FIT_CENTER
            }*/
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = TestNasaEarthFragment()
    }
}