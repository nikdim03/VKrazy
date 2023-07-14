package com.example.vkrazy.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PostItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(postItems: List<PostItemEntity>)

//    TODO:
//    experiment with returning flow from List<PostItemEntity>
//    read about page()
@Query("SELECT * FROM post_item_table LIMIT :count OFFSET :offset")
suspend fun getPosts(offset: Int, count: Int): List<PostItemEntity>
}
