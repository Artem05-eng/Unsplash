package com.example.unsplash.data

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Avatar(
    val small: String?,
    val medium: String?,
    val large: String?
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class User(
    val id: String,
    val updated_at: String,
    val username: String,
    val first_name: String,
    val last_name: String?,
    val location: String?,
    val total_likes: String,
    val total_photos: String,
    val total_collections: String,
    val profile_image: Avatar?
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Photo(
    val id: String,
    val created_at: String,
    val updated_at: String,
    val likes: String,
    val liked_by_user: Boolean,
    val description: String?,
    val user: User,
    val urls: Urls
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Urls(
    val regular: String,
    val thumb: String
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Collection(
    val id: String,
    val title: String,
    val description: String?,
    val published_at: String,
    val last_collected_at: String,
    val updated_at: String,
    val total_photos: String,
    val cover_photo: Photo?,
    val user: User
) : Parcelable

@JsonClass(generateAdapter = true)
data class WrapperPhotoSearch<T>(
    val results: List<T>
)

@JsonClass(generateAdapter = true)
data class WrapperPhoto<T>(
    val photo: T
)