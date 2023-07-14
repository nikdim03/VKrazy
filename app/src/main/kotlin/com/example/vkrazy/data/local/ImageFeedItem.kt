package com.example.vkrazy.data.local

data class ImageFeedItem(
    override val id: Int,
    val userPhoto: String?,
    val username: String,
    val postImage: String?,
    val likesText: String,
    val captionText: String
) : FeedItem, Equatable {
    override fun getViewType(): Int {
        return PHOTO_FEED_ITEM_TYPE
    }
}
