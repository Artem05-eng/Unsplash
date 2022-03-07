package com.example.unsplash.data

interface Repository {
    fun getUserProfile(callback: (User) -> Unit, callbackError: (Throwable?) -> Unit)

    fun getFavoritePhotos(
        username: String,
        callback: (List<Photo>) -> Unit,
        callbackError: (Throwable?) -> Unit
    )

    fun getUsersCollections(
        username: String,
        callback: (List<Collection>) -> Unit,
        callbackError: (Throwable?) -> Unit
    )

    fun likePhoto(id: String, callback: (Photo) -> Unit, callbackError: (Throwable?) -> Unit)

    fun unlikePhoto(id: String, callback: (Photo) -> Unit, callbackError: (Throwable?) -> Unit)

    fun searchCollection(query: String)

    fun getPhoto(id: String, callback: (Photo) -> Unit, callbackError: (Throwable?) -> Unit)

    fun getCollection(id: String, callback: (Collection) -> Unit)

    fun getCollectionPhotos(
        id: String,
        callback: (List<Photo>) -> Unit,
        callbackError: (Throwable?) -> Unit
    )

    fun getUsersPhotos(
        username: String,
        callback: (List<Photo>) -> Unit,
        callbackError: (Throwable?) -> Unit
    )
}