package com.example.vkrazy.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vkrazy.viewmodels.MyApplication

// Define a database class that holds the PostItem table and the DAO
@Database(entities = [PostItemEntity::class], version = 1, exportSchema = false)
abstract class PostItemDatabase : RoomDatabase() {

    abstract fun postItemDao(): PostItemDao

    companion object {
        // Singleton instance of the database
        @Volatile
        private var INSTANCE: PostItemDatabase? = null

        // Get or create the database instance
        fun getDatabase(context: Context): PostItemDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    PostItemDatabase::class.java,
                    "post_item_database"
                )
                    .fallbackToDestructiveMigration() // add this method to handle schema changes
                    .build()
                    .also { instance ->
                        INSTANCE = instance // assign the instance to the variable first
                        (context.applicationContext as MyApplication).feedComponent.inject(instance) // use the also scope function to perform the injection
                    }
            }
        }
    }
}
