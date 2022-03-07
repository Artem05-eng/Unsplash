package com.example.unsplash.database

object UnsplashContract {
    const val TABLE_NAME_PHOTO = "photos"
    const val TABLE_NAME_COLLECTION = "collections"
    const val TABLE_NAME_USER = "users"
    const val TABLE_NAME_USER1 = "users1"

    object CollumnPhoto {
        const val ID = "id_photo"
        const val LIKES = "likes"
        const val LIKED_BY_USER = "liked_by_user"
        const val DESCRIPTION = "description"
        const val CREATED_AT = "created_at_photo"
        const val UPDATED_AT = "updated_at_photo"
        const val USER_ID = "user_id_photo"
        const val PREV_KEY = "prev_key"
        const val NEXT_KEY = "next_key"
    }

    object CollumnCollection {
        const val ID = "id_collection"
        const val TITLE = "title"
        const val DESCRIPTION = "description_collection"
        const val TOTAL_PHOTOS = "total_photos"
        const val PUBLISHED_AT = "published_at"
        const val LAST_COLLECTED_AT = "last_collected_at"
        const val UPDATED_AT = "updated_at_collection"
        const val USER_ID = "user_id_collection"
        const val PREV_KEY = "prev_key"
        const val NEXT_KEY = "next_key"
        const val ID_PHOTO = "id_photo"
        const val LIKES_PHOTO = "likes"
        const val LIKED_BY_USER_PHOTO = "liked_by_user"
        const val DESCRIPTION_PHOTO = "description"
        const val CREATED_AT_PHOTO = "created_at_photo"
        const val UPDATED_AT_PHOTO = "updated_at_photo"
        const val USER_ID_PHOTO = "user_id_photo"
    }

    object CollumnUser {
        const val ID = "id_user"
        const val USERNAME = "username"
        const val FIRST_NAME = "first_name"
        const val LAST_NAME = "last_name"
        const val AVATAR = "avatar"
        const val LOCATION = "location"
        const val UPDATED_AT = "updated_at_user"
        const val TOTAL_LIKES = "total_likes"
        const val TOTAL_PHOTO = "total_photos"
        const val TOTAL_COLLECTIONS = "total_collections"

    }

    object CollumnUser1 {
        const val ID = "id_user"
        const val USERNAME = "username"
        const val FIRST_NAME = "first_name"
        const val LAST_NAME = "last_name"
        const val AVATAR = "avatar"
        const val LOCATION = "location"
        const val UPDATED_AT = "updated_at_user"
        const val TOTAL_LIKES = "total_likes"
        const val TOTAL_PHOTO = "total_photos"
        const val TOTAL_COLLECTIONS = "total_collections"

    }

}