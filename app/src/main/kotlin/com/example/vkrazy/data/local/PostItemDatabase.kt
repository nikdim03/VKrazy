package com.example.vkrazy.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vkrazy.viewmodels.MyApplication

@Database(entities = [PostItemEntity::class], version = 1, exportSchema = false)
abstract class PostItemDatabase : RoomDatabase() {
    abstract fun postItemDao(): PostItemDao

    companion object {
        @Volatile
        private var INSTANCE: PostItemDatabase? = null

        // get or create db instance
        fun getDatabase(context: Context): PostItemDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    PostItemDatabase::class.java,
                    POST_DB_NAME
                )
                    .fallbackToDestructiveMigration() // handle schema changes
                    .build()
                    .also { instance ->
                        INSTANCE = instance
                        (context.applicationContext as MyApplication).feedComponent.inject(instance)
                    }
            }
        }

        private const val POST_DB_NAME = "post_item_database"
        const val POST_ITEM_TABLE = "post_item_table"
    }
}
