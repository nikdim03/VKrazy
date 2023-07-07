package com.example.vkrazy.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PostItemDao {

    // Insert a list of post items into the table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(postItems: List<PostItemEntity>)

    // Query the first 20 post items from the table
    @Query("SELECT * FROM post_item LIMIT 20")
    suspend fun getFirst20(): List<PostItemEntity>
}
