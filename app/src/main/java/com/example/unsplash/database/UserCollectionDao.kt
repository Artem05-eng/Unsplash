package com.example.unsplash.database

import androidx.paging.PagingSource
import androidx.room.*
import com.example.unsplash.data.UserDataBaseClass1
import com.example.unsplash.data.UserWithCollectionRelations

@Dao
interface UserCollectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserDataBaseClass1>)

    @Query("DELETE FROM ${UnsplashContract.TABLE_NAME_USER1}")
    suspend fun clearUsers()


    @Transaction
    @Query("SELECT * FROM ${UnsplashContract.TABLE_NAME_USER1}")
    fun getPagingUsersWithCollection(): PagingSource<Int, UserWithCollectionRelations>


    @Transaction
    @Query("SELECT * FROM ${UnsplashContract.TABLE_NAME_USER1}, ${UnsplashContract.TABLE_NAME_COLLECTION} WHERE ${UnsplashContract.TABLE_NAME_COLLECTION}.${UnsplashContract.CollumnCollection.ID} = :collectionId")
    suspend fun getUserWithCollectionRelation(collectionId: String): UserWithCollectionRelations?
}