package com.example.vkrazy.viewmodels

import android.accounts.Account
import android.accounts.AccountManager
import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.authenticator.AuthenticationHelper
import com.example.vkrazy.R
import kotlinx.coroutines.flow.MutableStateFlow

class AuthorizationViewModel(private val application: Application) : AndroidViewModel(application) {
    private lateinit var accountManager: AccountManager
    private val _loginLiveData = MutableStateFlow<String?>(null)
    val loginLiveData = _loginLiveData

    fun getAuthUrl() = buildString {
        append("https://oauth.vk.com/authorize?")
        append("client_id=")
        append(application.getString(R.string.client))
        append("&group_ids=")
        append(application.getString(R.string.groups))
        append("&display=")
        append(application.getString(R.string.display))
        append("&redirect_uri=")
        append(application.getString(R.string.redirect_uri))
        append("&scope=")
        append(application.getString(R.string.scope))
        append("&response_type=")
        append(application.getString(R.string.response_type))
        append("&v=")
        append(application.getString(R.string.v))
    }

    fun handleUrl(url: String) {
        if (url.startsWith(application.resources.getString(R.string.redirect_uri).lowercase())) {
            processAccessToken(url)
        }
    }

    private fun processAccessToken(url: String) {
        val authToken = url.substringAfter("access_token=").substringBefore("&")
        Log.d("processAccessToken", authToken)
        val sharedPreferences =
            application.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(AUTH_TOKEN, authToken).apply()
        _loginLiveData.value = authToken
    }

    private fun getAccountsWithAuthToken(callback: (Account) -> Unit, activity: Activity) {
        accountManager.getAccountsByType(AuthenticationHelper.ACCOUNT_TYPE).forEach {
            val token = accountManager.peekAuthToken(it, AuthenticationHelper.TOKEN_TYPE)

            if (token != null) {
                callback.invoke(it)
            } else {
                Toast.makeText(activity, "${it.name} has no token", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        var AUTH_PREFERENCES = "auth_preferences"
        var AUTH_TOKEN = "auth_token"
    }
}
