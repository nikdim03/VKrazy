package com.example.vkrazy.viewmodels

import android.app.Application
import com.example.vkrazy.viewmodels.di.AppComponent
import com.example.vkrazy.viewmodels.di.DaggerAppComponent

class MyApplication : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }
}
