package com.example.unsplash.database

import androidx.paging.PagingSource
import androidx.room.*
import com.example.unsplash.data.UserDataBaseClass
import com.example.unsplash.data.UserWithPhotoRelations

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserDataBaseClass>)

    @Query("SELECT * FROM ${UnsplashContract.TABLE_NAME_USER} WHERE ${UnsplashContract.CollumnUser.ID} = :userId")
    suspend fun selectUserById(userId: String): UserDataBaseClass

    @Transaction
    @Query("SELECT * FROM ${UnsplashContract.TABLE_NAME_USER}")
    fun getPagingUsersWithPhoto(): PagingSource<Int, UserWithPhotoRelations>

    @Transaction
    @Query("SELECT * FROM ${UnsplashContract.TABLE_NAME_USER}, ${UnsplashContract.TABLE_NAME_PHOTO} WHERE ${UnsplashContract.TABLE_NAME_PHOTO}.${UnsplashContract.CollumnPhoto.ID} = :photoId")
    suspend fun getUserWithPhotoRelation(photoId: String): UserWithPhotoRelations?

    @Query("DELETE FROM ${UnsplashContract.TABLE_NAME_USER}")
    suspend fun clearUsers()
}