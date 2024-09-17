package com.manipur.khannasi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.manipur.khannasi.fragment.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "HiMayam"
        supportActionBar?.elevation = 7f
        supportActionBar?.subtitle = "Manipuri news and discussion"
        supportActionBar?.setIcon(R.drawable.himayam_logo)

        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.grey_50))
        toolbar.setTitleTextAppearance(this, R.style.ToolbarTitleText)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.navigation_home -> selectedFragment = HomeFragment()
                R.id.navigation_discussion -> selectedFragment = EditorFragment()
                R.id.navigation_account -> selectedFragment = UserDetailsFragment()
                R.id.navigation_test -> selectedFragment = UserDetailsFragment()
            }
            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.mainFragmentContainer, selectedFragment).commit()
            }
            true
        }
            // Set default selection
            bottomNavigation.selectedItemId = R.id.navigation_home
        }
    }