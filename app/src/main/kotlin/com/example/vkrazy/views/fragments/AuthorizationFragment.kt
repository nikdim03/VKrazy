package com.example.vkrazy.views.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vkrazy.R
import com.example.vkrazy.databinding.FragmentAuthorizationBinding
import com.example.vkrazy.viewmodels.AuthorizationViewModel
import kotlinx.coroutines.delay

class AuthorizationFragment : Fragment() {
    private lateinit var binding: FragmentAuthorizationBinding
    private val authorizationViewModel: AuthorizationViewModel by viewModels()

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
        lifecycleScope.launchWhenStarted {
            authorizationViewModel.goToFeedEventFlow.collect {
                delay(5000)
                goToFeed()
            }
        }
        setupWebView()
        loadAuthorizationUrl()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        binding.webView.apply {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    Log.d("setupWebView", "$url")
                    super.onPageFinished(view, url)
//                    вынести в вьюмодель
                    if (url.startsWith(getString(R.string.redirect_uri).lowercase())) {
                        handleAccessToken(url)
                    }
                }
            }
        }
    }

    private fun loadAuthorizationUrl() {
        binding.webView.loadUrl(
            authorizationViewModel.getAuthUrl(
                client = getString(R.string.client),
                groups = getString(R.string.groups),
                redirectUri = getString(R.string.redirect_uri),
                scope = getString(R.string.scope),
                v = getString(R.string.v)
            )
        )
    }

    private fun handleAccessToken(url: String) {
        Log.d("context", context.toString())
        context?.let {
            activity?.let { it1 ->
                authorizationViewModel.processAccessToken(
                    url, it,
                    it1
                )
            }
        }
    }

    private fun goToFeed() {
        Log.d("goToFeed", "goToFeed")
        findNavController().navigate(R.id.secondFragment)
    }
}
