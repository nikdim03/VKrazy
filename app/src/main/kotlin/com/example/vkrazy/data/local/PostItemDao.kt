package com.example.vkrazy.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PostItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(postItems: List<PostItemEntity>)

    @Query("SELECT * FROM post_item_table LIMIT 20")
    suspend fun getFirst20(): List<PostItemEntity>
}
