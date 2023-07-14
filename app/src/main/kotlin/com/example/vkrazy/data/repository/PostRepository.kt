package com.example.vkrazy.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.vkrazy.data.local.FeedItem
import com.example.vkrazy.data.local.ImageFeedItem
import com.example.vkrazy.data.local.PostItemDao
import com.example.vkrazy.data.local.PostItemEntity
import com.example.vkrazy.data.local.TextFeedItem
import com.example.vkrazy.data.remote.VKApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.lang.Integer.min
import java.net.URL

class PostRepository(
    private val apiService: VKApiService,
    private val postItemDao: PostItemDao,
    private val accessToken: String,
    private val context: Context
) {
    private var endOfLocalData = false
    private fun convertFeedItemsToEntities(feedItems: List<FeedItem>): List<PostItemEntity> {
        Log.d(TAG, "convertFeedItemsToEntities()")
        return feedItems.map { feedItem ->
            var likes = 0
            var ownerId: Long = 0
            var text = ""
            var attachments = ""
            when (feedItem) {
                is ImageFeedItem -> {
                    Log.d(TAG, "postImage = ${feedItem.postImage}")
                    likes = feedItem.likesText.replace(" likes", "").toInt()
                    ownerId = feedItem.username.toLong()
                    text = feedItem.captionText
                    attachments = saveImageFromUrl(feedItem.postImage.toString())
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
        Log.d(TAG, "getPostsFromInternet()")
        return try {
            val response = apiService.getNewsFeed(
                accessToken = accessToken,
                apiVersion = "5.131"
            )
            if (response.isSuccessful) {
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
        } catch (e: Exception) {
            Log.d(TAG, "No internet connection")
            null
        }
    }

    suspend fun savePosts(count: Int) {
        Log.d(TAG, "savePosts($count)")
        val posts = getPostsFromInternet()
        if (!posts.isNullOrEmpty()) {
            val entities = convertFeedItemsToEntities(posts)
            postItemDao.insertAll(entities.subList(count, min(count + 20, entities.size)))
        }
    }

    suspend fun getPosts(offset: Int, count: Int): List<FeedItem>? {
        Log.d(TAG, "getPosts($offset, $count)")
        val posts = if (endOfLocalData) getPostsFromInternet() else null
        if (!posts.isNullOrEmpty()) {
            savePosts(count)
            return posts
        } else {
            val entities = postItemDao.getPosts(offset, count)
            Log.d(TAG, "getPosts($offset, $count)\nentities: $entities")
            if (entities.isEmpty()) endOfLocalData = true
            return if (entities.isNotEmpty()) {
                entities.map { entity ->
                    val id = entity.id
                    val userPhoto = null
                    val username = "${entity.owner_id}"
                    val imageUrl = entity.attachments
                    val likesText = "${entity.likes} likes"
                    val captionText = entity.text
                    if (imageUrl != "") {
                        ImageFeedItem(id, userPhoto, username, imageUrl, likesText, captionText)
                    } else {
                        TextFeedItem(id, userPhoto, username, likesText, captionText)
                    }
                }
            } else {
                null
            }
        }
    }

    fun makePost(text: String, isPrivate: Boolean) {
        Log.d(TAG, "makePost(text = $text, isPrivate = $isPrivate)")
        if (isPrivate) {
            apiService.postFeedPost(text)
        } else {
            apiService.postFeedPost(text)
        }
    }

    private fun saveImageFromUrl(url: String): String {
        // Create a file name based on the url
        val fileName = url.substring(url.lastIndexOf("/") + 1)
        // Get the internal directory path
        val dirPath = context.filesDir.absolutePath
        // Create a file object with the full path
        val file = File(dirPath, fileName)
        // Use a coroutine to perform network and IO operations in the background thread
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Download the image from the url as a bitmap
                val bitmap = BitmapFactory.decodeStream(withContext(Dispatchers.IO) {
                    URL(url).openStream()
                })
                // Save the bitmap to the file as a JPEG format with 100% quality
                withContext(Dispatchers.IO) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return file.absolutePath
    }

    companion object {
        private const val TAG = "PostRepository"
    }
}
