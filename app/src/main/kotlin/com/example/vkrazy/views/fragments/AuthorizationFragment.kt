package com.example.vkrazy.views.fragments

import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.authenticator.AuthenticationHelper
import com.example.vkrazy.R
import com.example.vkrazy.databinding.FragmentAuthorizationBinding
import com.example.vkrazy.viewmodels.AuthorizationViewModel

class AuthorizationFragment : Fragment() {
    private lateinit var binding: FragmentAuthorizationBinding
    private val viewModel: AuthorizationViewModel by viewModels()
    private lateinit var accountManager: AccountManager

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthorizationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWebView()
        loadAuthUrl()
        handleAccountLogin()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        binding.webView.apply {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    Log.d("AuthorizationFragment", "setupWebView with: $url")
                    super.onPageFinished(view, url)
                    viewModel.handleUrl(url)
                }
            }
        }
    }

    private fun loadAuthUrl() {
        binding.webView.loadUrl(
            viewModel.getAuthUrl()
        )
    }

    private fun handleAccountLogin() {
        accountManager = AccountManager.get(requireActivity())
        viewModel.loginLiveData.observe(viewLifecycleOwner, Observer {
            addNewAccount(
                AuthenticationHelper.ACCOUNT_TYPE,
                AuthenticationHelper.TOKEN_TYPE, requireActivity()
            )
            goToFeed()
        })
    }

    private fun addNewAccount(accountType: String, authTokenType: String, activity: Activity) {
        Log.d("AuthorizationFragment", "addNewAccount")
        accountManager.addAccount(
            accountType, authTokenType, null, null, activity,
            { future ->
                try {
                    val result = future.result
                    Toast.makeText(activity, "$result", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, null
        )
    }

    private fun goToFeed() {
        Log.d("AuthorizationFragment", "goToFeed")
        findNavController().navigate(R.id.secondFragment)
    }
}
