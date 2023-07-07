package com.example.vkrazy.views.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.vkrazy.R
import com.example.vkrazy.databinding.ActivityMainBinding
import com.example.vkrazy.viewmodels.AuthorizationViewModel.Companion.AUTH_PREFERENCES
import com.example.vkrazy.viewmodels.AuthorizationViewModel.Companion.AUTH_TOKEN

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
//    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences = getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE)
        val navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment)
        val graph = navHostFragment.navController.navInflater.inflate(R.navigation.nav_graph)
        Log.d("MainActivity", "${sharedPreferences.getString(AUTH_TOKEN, null)}")
        if (sharedPreferences.getString(AUTH_TOKEN, null) == null) {
            graph.setStartDestination(R.id.firstFragment)
//            navHostFragment.navController.navigate(R.id.firstFragment)
        } else {
            graph.setStartDestination(R.id.secondFragment)
//            navHostFragment.navController.navigate(R.id.secondFragment)
        }
        navHostFragment.navController.graph = graph
    }
}
