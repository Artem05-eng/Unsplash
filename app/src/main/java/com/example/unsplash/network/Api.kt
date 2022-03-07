package com.example.unsplash.network

import com.example.unsplash.data.*
import com.example.unsplash.data.Collection
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface Api {

    //Get All Collections of Unsplash
    @GET("/collections")
    fun getCollections(
        @Query("page") page: Int
    ): Call<List<Collection>>

    //Get all photos of Unsplash
    @GET("/photos")
    fun getPhotos(
        @Query("order_by") order_by: String = "popular",
        @Query("page") page: Int
    ): Call<List<Photo>>

    //Get the user’s profile
    @GET("/me")
    fun getProfile(): Call<User>

    //List a user’s liked photos
    @GET("/users/{username}/likes")
    fun getFavoritePhotos(
        @Path("username") username: String
    ): Call<List<Photo>>

    //List a user’s photos
    @GET("/users/{username}/photos")
    fun getUsersPhoto(
        @Path("username") username: String
    ): Call<List<Photo>>

    //List a user’s collections
    @GET("/users/{username}/collections")
    fun getUsersCollections(
        @Path("username") username: String
    ): Call<List<Collection>>

    //Like a photo
    @POST("/photos/{id}/like")
    fun likePhoto(
        @Path("id") id: String
    ): Call<WrapperPhoto<Photo>>

    //Unlike a photo
    @DELETE("/photos/{id}/like")
    fun unlikePhoto(
        @Path("id") id: String
    ): Call<WrapperPhoto<Photo>>

    //Search photos by query
    @GET("/search/photos")
    fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Call<WrapperPhotoSearch<Photo>>

    //Get a photo
    @GET("/photos/{id}")
    fun getPhoto(
        @Path("id") id: String
    ): Call<Photo>

    //Search collections
    @GET("/search/collections")
    fun searchCollections(
        @Query("query") query: String
    ): Call<List<Collection>>

    //Get a collection
    @GET("/collections/{id}")
    fun getCollection(
        @Path("id") id: String
    ): Call<Collection>

    //Track a photo download
    @GET("/photos/{id}/download")
    suspend fun downloadPhoto(
        @Path("id") id: String
    ): ResponseBody

    //Get a collection’s photos
    @GET("/collections/{id}/photos")
    fun getCollectionPhotos(
        @Path("id") id: String
    ): Call<List<Photo>>

    @GET
    suspend fun downloadImage(
        @Url url: String
    ): ResponseBody
}