package com.example.vkrazy.data.local

interface FeedItem : Equatable {
    val id: Int
    fun getViewType(): Int
}

interface Equatable {
    override fun equals(other: Any?): Boolean
}

const val PHOTO_FEED_ITEM_TYPE = 0
const val TEXT_FEED_ITEM_TYPE = 1
