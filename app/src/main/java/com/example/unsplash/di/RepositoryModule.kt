package com.example.unsplash.di

import com.example.unsplash.data.Repository
import com.example.unsplash.data.RepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun providesRepository(impl: RepositoryImpl): Repository

}