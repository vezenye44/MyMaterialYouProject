package ru.geekbrains.mymaterialproject.ui.recycler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.geekbrains.mymaterialproject.databinding.FragmentRecyclerBinding

class RecyclerFragment : Fragment() {

    private lateinit var binding: FragmentRecyclerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arrayListOf(
            Data("Заголовок",type= TYPE_HEADER),
            Data("Earth",type=TYPE_EARTH),
            Data("Earth",type=TYPE_EARTH),
            Data("Mars", type= TYPE_MARS),
            Data("Earth",type=TYPE_EARTH),
            Data("Earth",type=TYPE_EARTH),
            Data("Earth",type=TYPE_EARTH),
            Data("Mars", type=TYPE_MARS)
        )
        binding.recyclerView.adapter = RecyclerAdapter(data)
    }

    companion object{
        fun newInstance() = RecyclerFragment()
    }
}