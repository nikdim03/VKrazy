package com.example.vkrazy.views.activities

import android.accounts.Account
import android.accounts.AccountManager
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import com.example.authenticator.AuthenticationHelper.ACCOUNT_TYPE
import com.example.authenticator.AuthenticationHelper.TOKEN_TYPE
import com.example.vkrazy.R
import com.example.vkrazy.databinding.ActivityMainBinding
import com.example.vkrazy.views.fragments.AuthorizationFragment
import com.example.vkrazy.views.fragments.FeedFragment
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
//    private lateinit var fragmentContainer: FrameLayout
    private lateinit var navContainer: Fragment
    private lateinit var accountManager: AccountManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        accountManager = AccountManager.get(this)

//        fragmentContainer = binding.fragmentContainer
//        navContainer = binding.navHostFragment

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        if (savedInstanceState == null) {
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        }
//        if (savedInstanceState == null) {
//            val loginFragment = AuthorizationFragment()
//            supportFragmentManager.commit {
//                replace(fragmentContainer.id, loginFragment)
//            }
//        }
//        if (savedInstanceState == null) {
//            navigateToFeed()
//        }
    }

//    private fun navigateToFeed() {
//        val feedFragment = FeedFragment()
//        supportFragmentManager.commit {
//            replace(fragmentContainer.id, feedFragment)
//        }
//    }
}
