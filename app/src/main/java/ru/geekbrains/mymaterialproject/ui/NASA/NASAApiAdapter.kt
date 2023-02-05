package ru.geekbrains.mymaterialproject.ui.NASA

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class NASAApiAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragments = arrayOf<Fragment>(
        TestNasaEarthFragment.newInstance(),
        TestNasaMarsFragment.newInstance(),
        TestNasaSystemFragment.newInstance()
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    companion object {
        private const val EARTH_FRAGMENT = 0
        private const val MARS_FRAGMENT = 1
        private const val SYSTEM_FRAGMENT = 2

        fun getNameByPosition(position: Int): String = when (position) {
            EARTH_FRAGMENT -> "Земля"
            MARS_FRAGMENT -> "Марс"
            SYSTEM_FRAGMENT -> "Система"
            else -> "Else branch"
        }
    }
}