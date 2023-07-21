package com.example.vkrazy.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkrazy.data.local.FeedItem
import com.example.vkrazy.data.local.ImageFeedItem
import com.example.vkrazy.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FeedViewModel(private val postRepository: PostRepository) : ViewModel() {
    private val _feedItems = MutableStateFlow<List<FeedItem>?>(null)
    val feedItems: StateFlow<List<FeedItem>?> = _feedItems
    var offset = 0

    init {
        val testData = listOf(
            ImageFeedItem(
                id = 1,
                userPhoto = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/TechCrunch_Disrupt_2019_%2848834434641%29_%28cropped%29.jpg/640px-TechCrunch_Disrupt_2019_%2848834434641%29_%28cropped%29.jpg",
                username = "willsmith",
                postImage = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/TechCrunch_Disrupt_2019_%2848834434641%29_%28cropped%29.jpg/640px-TechCrunch_Disrupt_2019_%2848834434641%29_%28cropped%29.jpg",
                likesText = "828,123 likes",
                captionText = "This is a caption for the post. It's actually a very long caption."
            ),
            // add more test data items here
        )
        _feedItems.value = testData
    }

    fun onViewCreated() {
//        fetch posts
        viewModelScope.launch {
            val response = postRepository.getPosts(offset, POST_COUNT_DEFAULT)
            _feedItems.value = appendPosts(_feedItems.value, response)
            offset += POST_COUNT_DEFAULT
        }
    }

    fun onDestroyView() {
//        save posts
        viewModelScope.launch {
            postRepository.savePosts(offset)
        }
    }

    private fun appendPosts(oldPosts: List<FeedItem>?, newPosts: List<FeedItem>?): List<FeedItem>? {
        Log.d(TAG, "appendPosts(oldPosts = $oldPosts, newPosts = $newPosts)")
        return if (oldPosts == null) {
            newPosts
        } else if (newPosts == null) {
            oldPosts
        } else {
            oldPosts + newPosts
        }
    }

    fun sendPost(text: String, isPrivate: Boolean) {
        postRepository.makePost("$text\n\nSent from VKrazy Mobile App", isPrivate)
    }

    companion object {
        const val POST_COUNT_DEFAULT = 20
        const val TAG = "FeedViewModel"
    }
}
