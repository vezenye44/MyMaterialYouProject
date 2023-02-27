package ru.geekbrains.mymaterialproject.ui.constraint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import ru.geekbrains.mymaterialproject.R
import ru.geekbrains.mymaterialproject.databinding.FragmentConstraintsBinding

class ConstraintsFragment : Fragment() {

    private var _binding: FragmentConstraintsBinding? = null
    private val binding get() = _binding!!

    private lateinit var longText : String
    private lateinit var shortText : String
    private val buttonListener : View.OnClickListener = View.OnClickListener {button ->
        with(binding) {
            button as MaterialButton
            if (button.text.equals(longText)) {
                button.text = shortText
            } else {
                button.text = longText
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConstraintsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        longText = requireActivity().getString(R.string.long_btn)
        shortText = requireActivity().getString(R.string.short_btn)

        with(binding) {
            button1.setOnClickListener(buttonListener)
            button2.setOnClickListener(buttonListener)
        }
    }

    companion object {
        fun newInstance() = ConstraintsFragment()
    }
}