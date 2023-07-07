package com.example.vkrazy.viewmodels

import android.app.Application
import com.example.vkrazy.viewmodels.di.DaggerFeedComponent
import com.example.vkrazy.viewmodels.di.FeedComponent
import com.example.vkrazy.viewmodels.di.FeedModule

class MyApplication : Application() {
    lateinit var feedComponent: FeedComponent

    override fun onCreate() {
        super.onCreate()
        feedComponent = DaggerFeedComponent.builder()
            .feedModule(FeedModule(this))
            .build()
    }
}
