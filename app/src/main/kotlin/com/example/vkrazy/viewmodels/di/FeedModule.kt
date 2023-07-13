package com.example.vkrazy.viewmodels.di

import android.app.Application
import android.content.Context
import com.example.vkrazy.data.local.PostItemDao
import com.example.vkrazy.data.local.PostItemDatabase
import com.example.vkrazy.data.remote.NetworkConstants.Companion.BASE_URL
import com.example.vkrazy.data.remote.VKApiService
import com.example.vkrazy.data.repository.PostRepository
import com.example.vkrazy.viewmodels.AuthorizationViewModel.Companion.AUTH_PREFERENCES
import com.example.vkrazy.viewmodels.AuthorizationViewModel.Companion.AUTH_TOKEN
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
            .baseUrl(BASE_URL)
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
    fun providePostRepository(
        apiService: VKApiService,
        postItemDao: PostItemDao
    ): PostRepository {
        val sharedPreferences = application.getSharedPreferences(
            AUTH_PREFERENCES,
            Context.MODE_PRIVATE
        )
        return PostRepository(
            apiService, postItemDao, sharedPreferences.getString(
                AUTH_TOKEN,
                ""
            ) ?: ""
        )
    }

    @Singleton
    @Provides
    fun provideFeedViewModel(postRepository: PostRepository): FeedViewModel {
        return FeedViewModel(postRepository)
    }
}
