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
//    TODO:
//    getposts(offset = 0, count = 20) - get first 20
//    getposts(offset = 20, count = 20) - get second 20
//    или page()
//    поэксперементировать с возвратом флоу от List<PostItemEntity>
    suspend fun getFirst20(): List<PostItemEntity>
}
