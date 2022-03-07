package com.example.unsplash.app

import android.app.Application
import com.example.unsplash.data.NotificationObject
import com.example.unsplash.database.UnsplashDatabase
import com.example.unsplash.di.DaggerUnsplashComponent
import com.example.unsplash.di.UnsplashComponent
import com.example.unsplash.di.UnsplashModule
import javax.inject.Inject

class App : Application() {

    @Inject
    lateinit var database: UnsplashDatabase
    lateinit var component: UnsplashComponent

    override fun onCreate() {
        super.onCreate()
        NotificationObject.create(this)
        component = DaggerUnsplashComponent.builder().unsplashModule(UnsplashModule(this)).build()
        component.inject(this)
    }
}