package com.example.vkrazy.views.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.vkrazy.R
import com.example.vkrazy.databinding.ActivityMainBinding
import com.example.vkrazy.viewmodels.AuthorizationViewModel.Companion.AUTH_PREFERENCES
import com.example.vkrazy.viewmodels.AuthorizationViewModel.Companion.AUTH_TOKEN

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController =
            (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment).navController
        val sharedPreferences = getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE)
        Log.d("MainActivity", "${sharedPreferences.getString(AUTH_TOKEN, null)}")
        if (sharedPreferences.getString(AUTH_TOKEN, null) == null) {
            navController.navigate(R.id.firstFragment)
        } else {
            navController.navigate(R.id.secondFragment)
        }
    }
}
