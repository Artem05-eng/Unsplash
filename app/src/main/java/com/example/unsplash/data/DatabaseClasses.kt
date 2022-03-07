package com.example.unsplash.data

import android.os.Parcelable
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.example.unsplash.database.UnsplashContract
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = UnsplashContract.TABLE_NAME_USER)
data class UserDataBaseClass(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = UnsplashContract.CollumnUser.ID)
    val id_user: String,
    @ColumnInfo(name = UnsplashContract.CollumnUser.UPDATED_AT)
    val updated_at: String,
    @ColumnInfo(name = UnsplashContract.CollumnUser.USERNAME)
    val username: String,
    @ColumnInfo(name = UnsplashContract.CollumnUser.FIRST_NAME)
    val first_name: String,
    @ColumnInfo(name = UnsplashContract.CollumnUser.LAST_NAME)
    val last_name: String?,
    @ColumnInfo(name = UnsplashContract.CollumnUser.LOCATION)
    val location: String?,
    @ColumnInfo(name = UnsplashContract.CollumnUser.TOTAL_LIKES)
    val total_likes: String,
    @ColumnInfo(name = UnsplashContract.CollumnUser.TOTAL_PHOTO)
    val total_photos: String,
    @ColumnInfo(name = UnsplashContract.CollumnUser.TOTAL_COLLECTIONS)
    val total_collections: String,
    @Embedded val profile_image: Avatar?
) : Parcelable

@Parcelize
@Entity(tableName = UnsplashContract.TABLE_NAME_USER1)
data class UserDataBaseClass1(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = UnsplashContract.CollumnUser1.ID)
    val id_user: String,
    @ColumnInfo(name = UnsplashContract.CollumnUser1.UPDATED_AT)
    val updated_at: String,
    @ColumnInfo(name = UnsplashContract.CollumnUser1.USERNAME)
    val username: String,
    @ColumnInfo(name = UnsplashContract.CollumnUser1.FIRST_NAME)
    val first_name: String,
    @ColumnInfo(name = UnsplashContract.CollumnUser1.LAST_NAME)
    val last_name: String?,
    @ColumnInfo(name = UnsplashContract.CollumnUser1.LOCATION)
    val location: String?,
    @ColumnInfo(name = UnsplashContract.CollumnUser1.TOTAL_LIKES)
    val total_likes: String,
    @ColumnInfo(name = UnsplashContract.CollumnUser1.TOTAL_PHOTO)
    val total_photos: String,
    @ColumnInfo(name = UnsplashContract.CollumnUser1.TOTAL_COLLECTIONS)
    val total_collections: String,
    @Embedded val profile_image: Avatar?
) : Parcelable

@Parcelize
@Entity(
    tableName = UnsplashContract.TABLE_NAME_PHOTO,
    foreignKeys = [ForeignKey(
        entity = UserDataBaseClass::class,
        parentColumns = [UnsplashContract.CollumnUser.ID],
        childColumns = [UnsplashContract.CollumnPhoto.USER_ID], onDelete = CASCADE
    )]
)
data class PhotoDataBaseClass(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = UnsplashContract.CollumnPhoto.ID)
    val id_photo: String,
    @ColumnInfo(name = UnsplashContract.CollumnPhoto.CREATED_AT)
    val created_at: String,
    @ColumnInfo(name = UnsplashContract.CollumnPhoto.PREV_KEY)
    var prev_key: Int?,
    @ColumnInfo(name = UnsplashContract.CollumnPhoto.NEXT_KEY)
    var next_key: Int?,
    @ColumnInfo(name = UnsplashContract.CollumnPhoto.UPDATED_AT)
    val updated_at: String,
    @ColumnInfo(name = UnsplashContract.CollumnPhoto.LIKES)
    val likes: String,
    @ColumnInfo(name = UnsplashContract.CollumnPhoto.LIKED_BY_USER)
    val liked_by_user: Boolean,
    @ColumnInfo(name = UnsplashContract.CollumnPhoto.DESCRIPTION)
    val description: String?,
    @ColumnInfo(name = UnsplashContract.CollumnPhoto.USER_ID)
    val user_id: String,
    @Embedded val urls: Urls
) : Parcelable

@Parcelize
@Entity(
    tableName = UnsplashContract.TABLE_NAME_COLLECTION,
    foreignKeys = [ForeignKey(
        entity = UserDataBaseClass1::class,
        parentColumns = [UnsplashContract.CollumnUser1.ID],
        childColumns = [UnsplashContract.CollumnCollection.USER_ID], onDelete = CASCADE
    )]
)
data class CollectionDataBaseClass(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = UnsplashContract.CollumnCollection.ID)
    val id_collection: String,
    @ColumnInfo(name = UnsplashContract.CollumnCollection.TITLE)
    val title: String,
    @ColumnInfo(name = UnsplashContract.CollumnCollection.DESCRIPTION)
    val description: String?,
    @ColumnInfo(name = UnsplashContract.CollumnCollection.PUBLISHED_AT)
    val published_at: String,
    @ColumnInfo(name = UnsplashContract.CollumnCollection.LAST_COLLECTED_AT)
    val last_collected_at: String,
    @ColumnInfo(name = UnsplashContract.CollumnCollection.UPDATED_AT)
    val updated_at: String,
    @ColumnInfo(name = UnsplashContract.CollumnCollection.TOTAL_PHOTOS)
    val total_photos: String,
    @ColumnInfo(name = UnsplashContract.CollumnCollection.USER_ID)
    val user_id: String,
    @ColumnInfo(name = UnsplashContract.CollumnCollection.PREV_KEY)
    var prev_key: Int?,
    @ColumnInfo(name = UnsplashContract.CollumnCollection.NEXT_KEY)
    var next_key: Int?,
    @ColumnInfo(name = UnsplashContract.CollumnCollection.ID_PHOTO)
    val id_collection_photo: String?,
    @ColumnInfo(name = UnsplashContract.CollumnCollection.LIKES_PHOTO)
    val likes_collection_photo: String?,
    @ColumnInfo(name = UnsplashContract.CollumnCollection.LIKED_BY_USER_PHOTO)
    val likes_by_user_collection_photo: Boolean?,
    @ColumnInfo(name = UnsplashContract.CollumnCollection.DESCRIPTION_PHOTO)
    val description_collection_photo: String?,
    @ColumnInfo(name = UnsplashContract.CollumnCollection.CREATED_AT_PHOTO)
    val createdat_collection_photo: String?,
    @ColumnInfo(name = UnsplashContract.CollumnCollection.UPDATED_AT_PHOTO)
    val updatedat_collection_photo: String?,
    @ColumnInfo(name = UnsplashContract.CollumnCollection.USER_ID_PHOTO)
    val userid_collection_photo: String?,
    @Embedded val urls: Urls?
) : Parcelable

@Parcelize
data class UserWithPhotoRelations(
    @Embedded val user: UserDataBaseClass,
    @Relation(
        entity = PhotoDataBaseClass::class,
        parentColumn = UnsplashContract.CollumnUser.ID,
        entityColumn = UnsplashContract.CollumnPhoto.USER_ID
    )
    val photo: List<PhotoDataBaseClass>
) : Parcelable

@Parcelize
data class UserWithCollectionRelations(
    @Embedded val user: UserDataBaseClass1,
    @Relation(
        entity = CollectionDataBaseClass::class,
        parentColumn = UnsplashContract.CollumnUser1.ID,
        entityColumn = UnsplashContract.CollumnCollection.USER_ID
    )
    val collection: List<CollectionDataBaseClass>
) : Parcelable