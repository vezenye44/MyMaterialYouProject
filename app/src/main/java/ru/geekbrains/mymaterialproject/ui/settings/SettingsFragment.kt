package ru.geekbrains.mymaterialproject.ui.settings

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import ru.geekbrains.mymaterialproject.R
import ru.geekbrains.mymaterialproject.databinding.FragmentSettingsBinding
import ru.geekbrains.mymaterialproject.ui.MainActivity

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences =
            requireActivity()?.getSharedPreferences(MainActivity.KEY_SP, Activity.MODE_PRIVATE)

        sharedPreferences?.let { preferences ->
            val position = preferences.getInt(EXTRA_POSITION, 0)
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
        }

        setTabSelectedListener()

    }

    private fun setTabSelectedListener() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                val position = binding.tabLayout.selectedTabPosition
                sharedPreferences?.let { preferences ->
                    preferences.edit().putInt(EXTRA_POSITION, position).apply()
                }

                tab?.text.let { text ->
                    when (text) {
                        getString(R.string.tab_item_strict_theme) -> {
                            sharedPreferences?.let { preferences ->
                                preferences.edit()
                                    .putString(MainActivity.EXTRA_THEME, MainActivity.THEME_STRICT)
                                    .apply()
                            }
                            requireActivity().recreate()
                        }
                        getString(R.string.tab_item_light_theme) -> {
                            sharedPreferences?.let { preferences ->
                                preferences.edit()
                                    .putString(MainActivity.EXTRA_THEME, MainActivity.THEME_LIGHT)
                                    .apply()
                            }
                            requireActivity().recreate()
                        }
                        getString(R.string.tab_item_dark_theme) -> {
                            sharedPreferences?.let { preferences ->
                                preferences.edit()
                                    .putString(MainActivity.EXTRA_THEME, MainActivity.THEME_DARK)
                                    .apply()
                            }
                            requireActivity().recreate()
                        }
                        else -> {}
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = SettingsFragment()
        const val EXTRA_POSITION = "EXTRA_POSITION"
    }
}