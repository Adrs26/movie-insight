package com.example.movieinsight.ui.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.movieinsight.R
import com.example.movieinsight.databinding.ActivityMainBinding

@SuppressLint("SourceLockedOrientationActivity")
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomNavigationView()
        setupOrientation()
    }

    private fun setupBottomNavigationView() {
        val navView = binding.bottomNavView
        val navController = findNavController(R.id.nav_host_main)
        navView.setupWithNavController(navController)
    }

    private fun setupOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}