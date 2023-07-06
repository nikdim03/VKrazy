package com.example.vkrazy.data.remote

import NewsFeedResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VKApiService {
    @GET("method/newsfeed.get")
    suspend fun getNewsFeed(
        @Query("access_token") accessToken: String,
        @Query("v") apiVersion: String,
        @Query("filters") filters: String = "post"
    ): Response<NewsFeedResponse>
}
