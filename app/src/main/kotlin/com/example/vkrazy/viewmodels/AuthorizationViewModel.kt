package com.example.vkrazy.viewmodels

import android.accounts.Account
import android.accounts.AccountManager
import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.authenticator.AuthenticationHelper.ACCOUNT_TYPE
import com.example.authenticator.AuthenticationHelper.TOKEN_TYPE
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthorizationViewModel(private val application: Application) : AndroidViewModel(application) {
    private lateinit var accountManager: AccountManager

    private val eventChannel = Channel<Unit>()
    val goToFeedEventFlow: Flow<Unit> = eventChannel.receiveAsFlow()

    fun getAuthUrl(
        client: String,
        groups: String,
        redirectUri: String,
        scope: String,
        v: String
    ) = buildString {
        append("https://oauth.vk.com/authorize?")
        append("client_id=")
        append(client)
        append("&group_ids=")
        append(groups)
        append("&display=mobile")
        append("&redirect_uri=")
        append(redirectUri)
        append("&scope=")
        append(scope)
        append("&response_type=token")
        append("&v=")
        append(v)
    }

    fun processAccessToken(url: String, context: Context, activity: Activity) {
////        val accountManager = AccountManager.get(getApplication<Application>().applicationContext)
////        Log.d("accountManager", accountManager.toString())
//        val accountName = "VKrazy VK Account"
//        val accountType = "com.example.vkrazy.vkaccount"
        val authToken = url.substringAfter("access_token=").substringBefore("&")
////
        Log.d("processAccessToken", authToken)
////        val account = Account(accountName, accountType)
////        Log.d("account", account.toString())
////        accountManager.addAccount(account)
////        accountManager.addAccountExplicitly(account, null, null)
////        accountManager.setAuthToken(account, accountType, authToken)
////        Log.d("processAccessToken", "made it")
//
//
//
//        val accountManager = AccountManager.get(context)
//        val account = Account(accountName, accountType)
//
//        // Set the user data associated with the account (optional)
//        val userData = Bundle()
//        userData.putString("key", "value")
//
//        // Add the account to the AccountManager
//        val accountAdded = accountManager.addAccountExplicitly(account, authToken, userData)
//
//        // Perform any additional operations if the account was added successfully
//        if (accountAdded) {
//            Log.d("accountAdded", accountManager.getPassword(account))
//            // Perform necessary actions, such as storing the account details
//            // or redirecting the user to the account setup process
//        } else {
//            Log.d("not added", "failure")
//            // Account addition failed, handle the error
//        }
        accountManager = AccountManager.get(application.applicationContext)
        Log.d("accountManager", accountManager.toString())
        addNewAccount(ACCOUNT_TYPE, TOKEN_TYPE, activity)
        ACCESS_TOKEN = authToken
        navigateToFeed()
    }

    private fun getAccountsWithAuthToken(callback: (Account) -> Unit, activity: Activity) {
        accountManager.getAccountsByType(ACCOUNT_TYPE).forEach {
            val token = accountManager.peekAuthToken(it, TOKEN_TYPE)

            if (token != null) {
                callback.invoke(it)
            } else {
                Toast.makeText(activity, "${it.name} has no token", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addNewAccount(accountType: String, authTokenType: String, activity: Activity) {
        Log.d("addNewAccount", "addNewAccount")
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

    private fun navigateToFeed() {
        viewModelScope.launch {
            eventChannel.send(Unit)
        }
    }

    companion object {
        var ACCESS_TOKEN = ""
    }
}
