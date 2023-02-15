package ru.geekbrains.mymaterialproject.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.geekbrains.mymaterialproject.R
import ru.geekbrains.mymaterialproject.databinding.MainActivityBinding
import ru.geekbrains.mymaterialproject.ui.NASA.NASAApiFragment
import ru.geekbrains.mymaterialproject.ui.constraint.ConstraintsFragment
import ru.geekbrains.mymaterialproject.ui.pictureOfTheDay.PictureOfTheDayFragment
import ru.geekbrains.mymaterialproject.ui.pictureOfTheDay.PictureOfTheDayFragmentByBehavior
import ru.geekbrains.mymaterialproject.ui.settings.SettingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)

        applyAppTheme()
        setContentView(binding.root)

        binding.navigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_POTD -> {
                    displayFragment(PictureOfTheDayFragmentByBehavior.newInstance()); true
                }
                R.id.action_favorite -> {
                    displayFragment(NASAApiFragment.newInstance()); true
                }
                R.id.action_settings -> {
                    displayFragment(SettingsFragment.newInstance()); true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            binding.navigationView.selectedItemId = R.id.action_POTD
        }

//        displayFragment(PictureOfTheDayFragmentByBehavior.newInstance())
//        displayFragment(ConstraintsFragment.newInstance())

    }

    private fun displayFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    private fun applyAppTheme() {
        getSharedPreferences(KEY_SP, MODE_PRIVATE).getString(EXTRA_THEME, THEME_STRICT).apply {
            when (this) {
                THEME_STRICT -> {
                    this@MainActivity.setTheme(R.style.MyStrictTheme)
                }
                THEME_LIGHT -> {
                    this@MainActivity.setTheme(R.style.MyLightTheme)
                }
                THEME_DARK -> {
                    this@MainActivity.setTheme(R.style.MyDarkTheme)
                }
            }
        }
    }


    companion object {
        const val KEY_SP = "EXTRA_THEME_SETTINGS"
        const val EXTRA_THEME = "EXTRA_THEME"
        const val THEME_STRICT = "THEME_STRICT"
        const val THEME_LIGHT = "THEME_LIGHT"
        const val THEME_DARK = "THEME_DARK"

    }

}