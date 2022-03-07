package com.example.unsplash.di

import com.example.unsplash.app.App
import com.example.unsplash.ui.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [UnsplashModule::class, RepositoryModule::class])
interface UnsplashComponent {
    fun inject(application: App)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: LentaFragment)
    fun inject(fragment: CollectionFragment)
    fun inject(fragment: DetailCollectionFragment)
    fun inject(fragment: DetailPhotoFragment)
}