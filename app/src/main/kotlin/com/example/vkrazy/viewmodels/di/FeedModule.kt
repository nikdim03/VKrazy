package com.example.vkrazy.viewmodels.di

import com.example.vkrazy.data.remote.VKApiService
import com.example.vkrazy.data.repository.PostRepository
import com.example.vkrazy.viewmodels.FeedViewModel
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class FeedModule {
    @Singleton
    @Provides
    fun provideVKApiService(): VKApiService {
        // Create a Retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.vk.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Return an implementation of VKApiService
        return retrofit.create(VKApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePostRepository(apiService: VKApiService): PostRepository {
        return PostRepository(apiService)
    }

    @Provides
    fun provideFeedViewModel(postRepository: PostRepository): FeedViewModel {
        return FeedViewModel(postRepository)
    }
}
