package com.example.vkrazy.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vkrazy.data.local.PostItemDatabase.Companion.POST_ITEM_TABLE

@Entity(tableName = POST_ITEM_TABLE)
data class PostItemEntity(
    @PrimaryKey val id: Int,
    val type: String,
    val source_id: Long,
    val date: Long,
    val short_text_rate: Double,
    val donut: Boolean, // convert to bool
    val comments: Int, // convert to int
    val marked_as_ads: Int,
    val can_set_category: Boolean,
    val can_doubt_category: Boolean,
    val attachments: String, // convert to json
    val is_favorite: Boolean,
    val likes: Int, // convert to int
    val owner_id: Long,
    val post_id: Int,
    val post_source: String, // convert to json
    val post_type: String,
    val reposts: Int, // convert to int
    val text: String,
    val views: Int // convert to int
)
