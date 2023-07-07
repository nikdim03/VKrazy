package com.example.vkrazy.data.repository

import com.example.vkrazy.data.local.FeedItem
import com.example.vkrazy.data.local.PostItemDao
import com.example.vkrazy.data.local.PostItemEntity
import com.example.vkrazy.data.remote.VKApiService

class PostRepository(
    private val apiService: VKApiService,
    private val postItemDao: PostItemDao
) {
    // add a function to convert a list of FeedItem objects to a list of PostItemEntity objects
    private fun convertFeedItemsToEntities(feedItems: List<FeedItem>): List<PostItemEntity> {
        return feedItems.map { feedItem ->
            PostItemEntity(
                id = 0, // assign a dummy id for now
                type = "", // assign a dummy type for now
                source_id = 0, // assign a dummy source id for now
                date = 0, // assign a dummy date for now
                short_text_rate = 0.0, // assign a dummy rate for now
                donut = false, // assign a dummy donut value for now
                comments = 0, // assign a dummy comment count for now
                marked_as_ads = 0, // assign a dummy ad value for now
                can_set_category = false, // assign a dummy category value for now
                can_doubt_category = false, // assign a dummy category value for now
                attachments = "", // assign a dummy attachment value for now
                is_favorite = false, // assign a dummy favorite value for now
                likes = feedItem.likesText.toInt(), // convert the likes text to an int value
                owner_id = feedItem.username.toLong(), // convert the username to a long value
                post_id = 0, // assign a dummy post id for now
                post_source = "", // assign a dummy post source value for now
                post_type = "", // assign a dummy post type value for now
                reposts = 0, // assign a dummy repost count for now
                text = feedItem.captionText, // use the caption text as the text value
                views = 0, // assign a dummy view count for now
            )
        }
    }

    private suspend fun getPostsFromInternet(): List<FeedItem>? {
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

    suspend fun saveFirst20Posts() {
        val posts = getPostsFromInternet()
        if (!posts.isNullOrEmpty()) {
            val entities = convertFeedItemsToEntities(posts)
            postItemDao.insertAll(entities.take(20))
        }
    }

    suspend fun getPosts(): List<FeedItem>? {
        val posts = getPostsFromInternet()
        if (!posts.isNullOrEmpty()) {
            return posts
        } else {
            // get the entities from the database
            val entities = postItemDao.getFirst20()
            return if (entities.isNotEmpty()) {
                entities.map { entity ->
                    val userPhoto = null
                    val username = "${entity.owner_id}"
                    val postImage = null // assign a dummy post image for now
                    val likesText = "${entity.likes}"
                    val captionText = entity.text
                    FeedItem(userPhoto, username, postImage, likesText, captionText)
                }
            } else {
                null
            }
        }
    }
}
