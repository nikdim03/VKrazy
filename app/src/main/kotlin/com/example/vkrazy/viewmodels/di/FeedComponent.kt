package com.example.vkrazy.viewmodels.di

import com.example.vkrazy.data.local.PostItemDao
import com.example.vkrazy.data.local.PostItemDatabase
import com.example.vkrazy.viewmodels.FeedViewModel
import com.example.vkrazy.views.fragments.FeedFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [FeedModule::class])
interface FeedComponent {
    fun inject(feedFragment: FeedFragment)
    fun inject(feedViewModel: FeedViewModel)
    fun inject(postItemDao: PostItemDao)
    fun inject(postItemDatabase: PostItemDatabase)
}
