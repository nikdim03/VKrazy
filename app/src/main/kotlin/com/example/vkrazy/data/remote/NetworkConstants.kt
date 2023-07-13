package com.example.vkrazy.data.remote

class NetworkConstants {
    companion object {
        const val BASE_URL = "https://api.vk.com/"
        private const val METHOD = "method"
        const val NEWSFEED_REQUEST = "$METHOD/newsfeed.get"
    }
}
