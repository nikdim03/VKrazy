package com.example.vkrazy.data.local

data class TextFeedItem(
    override val id: Int,
    val userPhoto: String?,
    val username: String,
    val likesText: String,
    val captionText: String
) : FeedItem, Equatable {
    override fun getViewType(): Int {
        return TEXT_FEED_ITEM_TYPE
    }
}
