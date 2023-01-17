package ru.geekbrains.mymaterialproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.geekbrains.mymaterialproject.R
import ru.geekbrains.mymaterialproject.databinding.MainActivityBinding
import ru.geekbrains.mymaterialproject.ui.pictureOfTheDay.PictureOfTheDayFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                .commitNow()
        }
    }

}