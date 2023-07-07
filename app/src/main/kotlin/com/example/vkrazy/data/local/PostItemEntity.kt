package com.example.vkrazy.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post_item")
data class PostItemEntity(
    @PrimaryKey val id: Int,
    val type: String,
    val source_id: Long,
    val date: Long,
    val short_text_rate: Double,
    val donut: Boolean, // convert Donut to a Boolean value
    val comments: Int, // convert CommentStats to an Int value
    val marked_as_ads: Int,
    val can_set_category: Boolean,
    val can_doubt_category: Boolean,
    val attachments: String, // convert List<Attachment> to a JSON string
    val is_favorite: Boolean,
    val likes: Int, // convert LikeStats to an Int value
    val owner_id: Long,
    val post_id: Int,
    val post_source: String, // convert PostSource to a JSON string
    val post_type: String,
    val reposts: Int, // convert RepostStats to an Int value
    val text: String,
    val views: Int // convert ViewStats to an Int value
)
