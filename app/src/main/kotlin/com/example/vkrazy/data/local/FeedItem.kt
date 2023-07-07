package com.example.vkrazy.data.local

data class FeedItem(
    val id: Int,
    val userPhoto: String?,
    val username: String,
    val postImage: String?,
    val likesText: String,
    val captionText: String
)
