package com.example.moexclient

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.gms.ads.MobileAds


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {}
        setContentView(R.layout.activity_main)
        setupActionBar()
    }
    override fun onSupportNavigateUp(): Boolean {
        return findNavController(this, R.id.nav_host_fragment).navigateUp()
    }
    private fun setupActionBar() {
        val host = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val navController = (host as NavHostFragment).navController
        //  IDs of fragments you want without the ActionBar home/up button
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.chartFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}