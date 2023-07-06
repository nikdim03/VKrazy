package com.example.authenticator

 object AuthenticationHelper {

     //universal account type for all apps now using this specific module
     const val ACCOUNT_TYPE = "com.example.vkrazy"

     //this value doesn't matter unless you want users with specialized access tokens (read only, full access, etc)
     const val TOKEN_TYPE = "type of token"
}
