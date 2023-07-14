package com.example.vkrazy.data.repository

import android.util.Log
import com.example.vkrazy.data.local.FeedItem
import com.example.vkrazy.data.local.ImageFeedItem
import com.example.vkrazy.data.local.PostItemDao
import com.example.vkrazy.data.local.PostItemEntity
import com.example.vkrazy.data.local.TextFeedItem
import com.example.vkrazy.data.remote.VKApiService

class PostRepository(
    private val apiService: VKApiService,
    private val postItemDao: PostItemDao,
    private val accessToken: String
) {
    private fun convertFeedItemsToEntities(feedItems: List<FeedItem>): List<PostItemEntity> {
        Log.d("PostRepository", "convertFeedItemsToEntities()")
        return feedItems.map { feedItem ->
            var likes = 0
            var ownerId: Long = 0
            var text = ""
            var attachments = ""
            when (feedItem) {
                is ImageFeedItem -> {
                    likes = feedItem.likesText.replace(" likes", "").toInt()
                    ownerId = feedItem.username.toLong()
                    text = feedItem.captionText
                    attachments = feedItem.postImage.toString()
                }

                is TextFeedItem -> {
                    likes = feedItem.likesText.replace(" likes", "").toInt()
                    ownerId = feedItem.username.toLong()
                    text = feedItem.captionText
                }
            }
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
                attachments = attachments,
                is_favorite = false,
                likes = likes,
                owner_id = ownerId,
                post_id = 0,
                post_source = "",
                post_type = "",
                reposts = 0,
                text = text,
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
                if (postImage != null) {
                    ImageFeedItem(id, userPhoto, username, postImage, likesText, captionText)
                } else {
                    TextFeedItem(id, userPhoto, username, likesText, captionText)
                }
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
                    val postImage = entity.attachments
                    val likesText = "${entity.likes} likes"
                    val captionText = entity.text
                    if (postImage != "null") {
                        ImageFeedItem(id, userPhoto, username, postImage, likesText, captionText)
                    } else {
                        TextFeedItem(id, userPhoto, username, likesText, captionText)
                    }
                }
            } else {
                null
            }
        }
    }
}
