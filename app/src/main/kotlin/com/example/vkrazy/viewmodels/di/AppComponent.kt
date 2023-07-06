package com.example.vkrazy.viewmodels.di

import com.example.vkrazy.viewmodels.FeedViewModel
import com.example.vkrazy.views.fragments.FeedFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [FeedModule::class])
interface AppComponent {
    fun inject(feedFragment: FeedFragment)
    fun inject(feedViewModel: FeedViewModel)
}
