package ru.geekbrains.mymaterialproject.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.geekbrains.mymaterialproject.R
import ru.geekbrains.mymaterialproject.databinding.MainActivityBinding
import ru.geekbrains.mymaterialproject.ui.pictureOfTheDay.PictureOfTheDayFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
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

        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                .commitNow()
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