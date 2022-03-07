package com.example.unsplash.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.unsplash.data.CollectionDataBaseClass
import com.example.unsplash.data.PhotoDataBaseClass
import com.example.unsplash.data.UserDataBaseClass
import com.example.unsplash.data.UserDataBaseClass1

@Database(
    entities = [
        PhotoDataBaseClass::class,
        CollectionDataBaseClass::class,
        UserDataBaseClass::class,
        UserDataBaseClass1::class],
    version = UnsplashDatabase.DB_VERSION
)
abstract class UnsplashDatabase : RoomDatabase() {

    abstract fun photosDao(): PhotoDao
    abstract fun collectionsDao(): CollectionDao
    abstract fun usersDao(): UserDao
    abstract fun userCollectionDao(): UserCollectionDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "unsplash-database"
    }
}