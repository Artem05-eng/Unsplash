package com.example.unsplash.di

import androidx.room.Room
import com.example.unsplash.app.App
import com.example.unsplash.database.*
import com.example.unsplash.network.Api
import com.example.unsplash.network.CustomInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Inject
import javax.inject.Singleton

@Module
class UnsplashModule @Inject constructor(val app: App) {

    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        .addNetworkInterceptor(CustomInterceptor())
        .followRedirects(true)
        .build()

    @Singleton
    @Provides
    fun providesNetwork(okHttpClient: OkHttpClient): Api = Retrofit.Builder()
        .baseUrl("https://api.unsplash.com/")
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okHttpClient)
        .build().create()

    @Singleton
    @Provides
    fun providesDatabase(): UnsplashDatabase {
        return Room.databaseBuilder(
            app,
            UnsplashDatabase::class.java,
            UnsplashDatabase.DB_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun providesPhotoDao(db: UnsplashDatabase): PhotoDao = db.photosDao()

    @Singleton
    @Provides
    fun providesCollectionDao(db: UnsplashDatabase): CollectionDao = db.collectionsDao()

    @Singleton
    @Provides
    fun providesUserDao(db: UnsplashDatabase): UserDao = db.usersDao()

    @Singleton
    @Provides
    fun providesUser1Dao(db: UnsplashDatabase): UserCollectionDao = db.userCollectionDao()
}