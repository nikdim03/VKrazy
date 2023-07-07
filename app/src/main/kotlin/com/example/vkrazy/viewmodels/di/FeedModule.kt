package com.example.vkrazy.viewmodels.di

import android.app.Application
import com.example.vkrazy.data.local.PostItemDao
import com.example.vkrazy.data.local.PostItemDatabase
import com.example.vkrazy.data.remote.VKApiService
import com.example.vkrazy.data.repository.PostRepository
import com.example.vkrazy.viewmodels.FeedViewModel
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class FeedModule(private val application: Application) {
    @Singleton
    @Provides
    fun provideVKApiService(): VKApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.vk.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(VKApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideApplication(): Application {
        return application
    }

    @Singleton
    @Provides
    fun providePostItemDatabase(application: Application): PostItemDatabase {
        return PostItemDatabase.getDatabase(application)
    }

    @Singleton
    @Provides
    fun providePostItemDao(database: PostItemDatabase): PostItemDao {
        return database.postItemDao()
    }

    @Singleton
    @Provides
    fun providePostRepository(apiService: VKApiService, postItemDao: PostItemDao): PostRepository {
        return PostRepository(apiService, postItemDao)
    }

    @Singleton
    @Provides
    fun provideFeedViewModel(postRepository: PostRepository): FeedViewModel {
        return FeedViewModel(postRepository)
    }
}
