package com.example.vkrazy.data.repository

import com.example.vkrazy.data.local.FeedItem
import com.example.vkrazy.data.remote.VKApiService
import javax.inject.Inject

class PostRepository(private val apiService: VKApiService) {
//    add db cache
    suspend fun getPosts(): List<FeedItem>? {
        val response = apiService.getNewsFeed(
            accessToken = "vk1.a.CqqPkIybeCnx051F4GB_rXJTy6C0CGtHKeYQKM_tfz7G3a6vX2H_-UPSdZCP_1P_echzo0_3IBPSIhjX9fT95LBb4qcmk7v_7Ip88pDEPhJyDJoVxm2EqVbHMK9-L4T9gIXpCGtcej3MrVfyB6f3DaWo8u6wr06LJhsM7tFFiCbDzk8sWjc-4X484Bc6mQNv0jmB8gilofXzZGB4bERoYA",
            apiVersion = "5.131"
        )

        return if (response.isSuccessful) {
            val newsFeedResponse = response.body()
            val newsItems = newsFeedResponse?.response?.items

            newsItems?.map { postItem ->
                val userPhoto = null
                val username = "${postItem.owner_id}"
                val postImage =
                    postItem.attachments.firstOrNull { it.type == "photo" }?.photo?.sizes?.last()?.url
                val likesText = "${postItem.likes.count}"
                val captionText = postItem.text
                FeedItem(userPhoto, username, postImage, likesText, captionText)
            }
        } else {
            null
        }
    }
}
