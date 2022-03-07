package com.example.unsplash.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.unsplash.data.PhotoDataBaseClass

@Dao
interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(photos: List<PhotoDataBaseClass>)

    @Query("SELECT * FROM ${UnsplashContract.TABLE_NAME_PHOTO}")
    suspend fun getAllPhotos(): List<PhotoDataBaseClass>

    @Query("SELECT * FROM ${UnsplashContract.TABLE_NAME_PHOTO} WHERE ${UnsplashContract.CollumnPhoto.DESCRIPTION} LIKE '%' ||  :name || '%'")
    suspend fun searchPhotoByName(name: String): List<PhotoDataBaseClass>?

    @Query("SELECT * FROM ${UnsplashContract.TABLE_NAME_PHOTO} WHERE ${UnsplashContract.CollumnPhoto.ID} = :photoId")
    suspend fun selectPhotoById(photoId: Long): PhotoDataBaseClass?

    @Query("DELETE FROM ${UnsplashContract.TABLE_NAME_PHOTO}")
    suspend fun clearPhotos()
}