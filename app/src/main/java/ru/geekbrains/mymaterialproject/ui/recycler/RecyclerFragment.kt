package ru.geekbrains.mymaterialproject.ui.recycler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.geekbrains.mymaterialproject.databinding.FragmentRecyclerBinding

class RecyclerFragment : Fragment() {

    private lateinit var binding: FragmentRecyclerBinding
    private lateinit var adapter: RecyclerAdapter
    private val data = arrayListOf(
        Data("Заголовок",type= TYPE_HEADER),
        Data("Земля",type=TYPE_EARTH),
        Data("Земля",type=TYPE_EARTH),
        Data("Марс", type= TYPE_MARS),
        Data("Земля",type=TYPE_EARTH),
        Data("Земля",type=TYPE_EARTH),
        Data("Земля",type=TYPE_EARTH),
        Data("Марс", type=TYPE_MARS)
    )

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

        adapter = RecyclerAdapter(data, callbackAdd, callbackRemove)
        binding.recyclerView.adapter = adapter
    }

    private val callbackAdd = AddItem {
        data.add(it, Data("Mars(New)", type= TYPE_MARS))
        adapter.setListDataAdd(data,it)
    }

    private val callbackRemove = RemoveItem {
        data.removeAt(it)
        adapter.setListDataRemove(data,it)
    }

    companion object{
        fun newInstance() = RecyclerFragment()
    }
}