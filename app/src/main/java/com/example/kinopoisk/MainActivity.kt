package com.example.kinopoisk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener)
        bottomNavigationView.selectedItemId = R.id.kinopoiskHD_page
        supportFragmentManager.beginTransaction().replace(R.id.main_fragment_container, KinopoiskHDFragment()).commit()
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selected: Fragment? = null
        when (item.itemId) {
            R.id.afiche_page -> selected = AficheFragment()
            R.id.kinopoiskHD_page -> selected = KinopoiskHDFragment()
            R.id.search_page -> selected = SearchFragment()
            R.id.profile_page -> selected = ProfileFragment()
        }
        supportFragmentManager.beginTransaction().replace(R.id.main_fragment_container, selected!!).commit()
        true
    }
}