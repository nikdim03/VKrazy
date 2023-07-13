package com.example.vkrazy.data.remote

import NewsFeedResponse
import com.example.vkrazy.data.remote.NetworkConstants.Companion.NEWSFEED_REQUEST
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VKApiService {
    @GET(NEWSFEED_REQUEST)
    suspend fun getNewsFeed(
        @Query("access_token") accessToken: String,
        @Query("v") apiVersion: String,
        @Query("filters") filters: String = "post"
    ): Response<NewsFeedResponse>
}
