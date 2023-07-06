package com.example.authenticator

import android.accounts.Account
import android.accounts.AccountAuthenticatorActivity
import android.accounts.AccountManager
import android.app.Activity
import com.example.authenticator.databinding.AuthMainBinding
import android.os.Bundle
import com.example.authenticator.AuthenticationHelper.TOKEN_TYPE
import com.example.authenticator.Authenticator.Companion.ARG_IS_ADDING_NEW_ACCOUNT
import java.util.*

/**
 * Called by the Authenticator and in charge of identification of the user.
 * It sends back to the Authenticator the result of a user trying to authenticate.
 */
class AuthenticatorActivity : AccountAuthenticatorActivity() {

    private lateinit var accountManager: AccountManager
    private lateinit var binding: AuthMainBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_main)
        accountManager = AccountManager.get(this)

        binding.addNewAccount.setOnClickListener {
            finishLogin(binding.username.text.toString(), binding.password.text.toString())
        }
    }

    /**
     * As part of this simple POC, this method will either:
     *
     * 1) explicitly create an account for a new user. This flow happens when you go through the flow of the APP -
     * the account will be made with a username, password and an auth token
     * OR
     * 2) update the existing account with a new password. This flow happens when you go through existing accounts on the DEVICE and then -
     * click on your custom authenticator
     */
    private fun finishLogin(username: String, password: String) {
        val account = Account(username, AuthenticationHelper.ACCOUNT_TYPE)

        if (intent.getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            val authtoken = UUID.randomUUID().toString()
            val authtokenType = TOKEN_TYPE
            accountManager.addAccountExplicitly(account, password, null)
            accountManager.setAuthToken(account, authtokenType, authtoken)
        } else {
            //updates password, this is not a new account but rather an existing account (flow 2)
            accountManager.setPassword(account, password)
        }

        setAccountAuthenticatorResult(intent.extras)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
