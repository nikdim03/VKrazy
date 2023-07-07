package com.example.vkrazy.data.repository

import android.util.Log
import com.example.vkrazy.data.local.FeedItem
import com.example.vkrazy.data.local.PostItemDao
import com.example.vkrazy.data.local.PostItemEntity
import com.example.vkrazy.data.remote.VKApiService

class PostRepository(
    private val apiService: VKApiService,
    private val postItemDao: PostItemDao,
    private val accessToken: String
) {
    private fun convertFeedItemsToEntities(feedItems: List<FeedItem>): List<PostItemEntity> {
        Log.d("PostRepository", "convertFeedItemsToEntities()")
        return feedItems.map { feedItem ->
            PostItemEntity(
                id = feedItem.id,
                type = "",
                source_id = 0,
                date = 0,
                short_text_rate = 0.0,
                donut = false,
                comments = 0,
                marked_as_ads = 0,
                can_set_category = false,
                can_doubt_category = false,
                attachments = "",
                is_favorite = false,
                likes = feedItem.likesText.replace(" likes", "").toInt(),
                owner_id = feedItem.username.toLong(),
                post_id = 0,
                post_source = "",
                post_type = "",
                reposts = 0,
                text = feedItem.captionText,
                views = 0,
            )
        }
    }

    private suspend fun getPostsFromInternet(): List<FeedItem>? {
        Log.d("PostRepository", "getPostsFromInternet()")
        val response = apiService.getNewsFeed(
            accessToken = accessToken,
            apiVersion = "5.131"
        )
        return if (response.isSuccessful) {
            val newsFeedResponse = response.body()
            val newsItems = newsFeedResponse?.response?.items
            newsItems?.map { postItem ->
                val id = postItem.id
                val userPhoto = null
                val username = "${postItem.owner_id}"
                val postImage =
                    postItem.attachments.firstOrNull { it.type == "photo" }?.photo?.sizes?.last()?.url
                val likesText = "${postItem.likes.count} likes"
                val captionText = postItem.text
                FeedItem(id, userPhoto, username, postImage, likesText, captionText)
            }
        } else {
            null
        }
    }

    suspend fun saveFirst20Posts() {
        Log.d("PostRepository", "saveFirst20Posts()")
        val posts = getPostsFromInternet()
        if (!posts.isNullOrEmpty()) {
            val entities = convertFeedItemsToEntities(posts)
            postItemDao.insertAll(entities.take(20))
        }
    }

    suspend fun getPosts(): List<FeedItem>? {
        Log.d("PostRepository", "getPosts()")
        val posts = getPostsFromInternet()
        if (!posts.isNullOrEmpty()) {
            return posts
        } else {
            val entities = postItemDao.getFirst20()
            return if (entities.isNotEmpty()) {
                entities.map { entity ->
                    val id = entity.id
                    val userPhoto = null
                    val username = "${entity.owner_id}"
                    val postImage = null
                    val likesText = "${entity.likes} likes"
                    val captionText = entity.text
                    FeedItem(id, userPhoto, username, postImage, likesText, captionText)
                }
            } else {
                null
            }
        }
    }
}
