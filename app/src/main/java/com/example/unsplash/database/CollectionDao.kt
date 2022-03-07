package com.example.unsplash.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.unsplash.data.CollectionDataBaseClass

@Dao
interface CollectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollections(collections: List<CollectionDataBaseClass>)

    @Query("SELECT * FROM ${UnsplashContract.TABLE_NAME_COLLECTION}")
    suspend fun getAllCollections(): List<CollectionDataBaseClass>

    @Query("SELECT * FROM ${UnsplashContract.TABLE_NAME_COLLECTION} WHERE ${UnsplashContract.CollumnCollection.ID} = :collectionId")
    suspend fun selectCollectionById(collectionId: Long): CollectionDataBaseClass?

    @Query("DELETE FROM ${UnsplashContract.TABLE_NAME_COLLECTION}")
    suspend fun clearCollections()
}