package com.example.vkrazy.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkrazy.data.local.FeedItem
import com.example.vkrazy.data.repository.PostRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class FeedViewModel(private val postRepository: PostRepository) : ViewModel() {
    private val _feedItems = MutableLiveData<List<FeedItem>?>()
    val feedItems: MutableLiveData<List<FeedItem>?> = _feedItems

    init {
        // Initialize the feed items with test data or fetch it from a data source
        val testData = listOf(
            FeedItem(
                "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/TechCrunch_Disrupt_2019_%2848834434641%29_%28cropped%29.jpg/640px-TechCrunch_Disrupt_2019_%2848834434641%29_%28cropped%29.jpg",
                "willsmith",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/TechCrunch_Disrupt_2019_%2848834434641%29_%28cropped%29.jpg/640px-TechCrunch_Disrupt_2019_%2848834434641%29_%28cropped%29.jpg",
                "828,123 likes",
                "This is a caption for the post. It's actually a very long caption."
            ),
            // Add more test data items here
        )
        _feedItems.value = testData
    }

    fun onViewCreated() {
//        fetch posts
        viewModelScope.launch {
            val response = postRepository.getPosts()
            _feedItems.value = response
        }
    }
}
