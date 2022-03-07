package com.example.unsplash.converters

import com.example.unsplash.data.*
import com.example.unsplash.data.Collection


fun photoToPhotoDatabaseClass(photo: Photo): PhotoDataBaseClass {
    return PhotoDataBaseClass(
        id_photo = photo.id,
        created_at = photo.created_at,
        updated_at = photo.updated_at,
        likes = photo.likes,
        liked_by_user = photo.liked_by_user,
        description = photo.description,
        user_id = photo.user!!.id,
        urls = photo.urls,
        next_key = null,
        prev_key = null
    )
}

fun collectionToCollectionDatabaseClass(collection: Collection): CollectionDataBaseClass {
    return CollectionDataBaseClass(
        id_collection = collection.id,
        title = collection.title,
        description = collection.description,
        published_at = collection.published_at,
        last_collected_at = collection.last_collected_at,
        updated_at = collection.updated_at,
        total_photos = collection.total_photos,
        user_id = collection.user.id,
        next_key = null,
        prev_key = null,
        createdat_collection_photo = collection.cover_photo?.created_at,
        updatedat_collection_photo = collection.cover_photo?.updated_at,
        userid_collection_photo = collection.cover_photo?.user?.id,
        likes_by_user_collection_photo = collection.cover_photo?.liked_by_user,
        likes_collection_photo = collection.cover_photo?.likes,
        description_collection_photo = collection.cover_photo?.description,
        id_collection_photo = collection.cover_photo?.id,
        urls = collection.cover_photo?.urls
    )
}


fun userToUserDatabaseClass(user: User): UserDataBaseClass {
    return UserDataBaseClass(
        id_user = user.id,
        updated_at = user.updated_at,
        username = user.username,
        first_name = user.first_name,
        last_name = user.last_name,
        location = user.location,
        total_likes = user.total_likes,
        total_photos = user.total_photos,
        total_collections = user.total_collections,
        profile_image = user.profile_image
    )
}

fun userToUserDatabaseClass1(user: User): UserDataBaseClass1 {
    return UserDataBaseClass1(
        id_user = user.id,
        updated_at = user.updated_at,
        username = user.username,
        first_name = user.first_name,
        last_name = user.last_name,
        location = user.location,
        total_likes = user.total_likes,
        total_photos = user.total_photos,
        total_collections = user.total_collections,
        profile_image = user?.profile_image
    )
}

fun userDataBaseToUser(userDataBaseClass: UserDataBaseClass): User {
    return User(
        id = userDataBaseClass.id_user,
        updated_at = userDataBaseClass.updated_at,
        username = userDataBaseClass.username,
        first_name = userDataBaseClass.first_name,
        last_name = userDataBaseClass.last_name,
        location = userDataBaseClass.location,
        total_likes = userDataBaseClass.total_likes,
        total_photos = userDataBaseClass.total_photos,
        total_collections = userDataBaseClass.total_collections,
        profile_image = userDataBaseClass.profile_image
    )
}

fun userDataBase1ToUser(userDataBaseClass: UserDataBaseClass1): User {
    return User(
        id = userDataBaseClass.id_user,
        updated_at = userDataBaseClass.updated_at,
        username = userDataBaseClass.username,
        first_name = userDataBaseClass.first_name,
        last_name = userDataBaseClass.last_name,
        location = userDataBaseClass.location,
        total_likes = userDataBaseClass.total_likes,
        total_photos = userDataBaseClass.total_photos,
        total_collections = userDataBaseClass.total_collections,
        profile_image = userDataBaseClass.profile_image
    )
}

fun userWithPhotoToPhoto(userWithPhotoRelations: UserWithPhotoRelations): List<Photo> {
    return userWithPhotoRelations.photo.map {
        Photo(
            id = it.id_photo,
            created_at = it.created_at,
            updated_at = it.updated_at,
            description = it.description,
            likes = it.likes,
            liked_by_user = it.liked_by_user,
            urls = it.urls,
            user = userDataBaseToUser(userWithPhotoRelations.user)
        )
    }
}

fun photoToUserWithPhotos(photo: Photo): UserWithPhotoRelations {
    return UserWithPhotoRelations(
        userToUserDatabaseClass(photo.user),
        listOf(photoToPhotoDatabaseClass(photo))
    )
}

fun photoDataBaseClassToPhoto(
    photoDataBaseClass: PhotoDataBaseClass,
    userDataBaseClass: UserDataBaseClass
): Photo {
    return Photo(
        id = photoDataBaseClass.id_photo,
        created_at = photoDataBaseClass.created_at,
        updated_at = photoDataBaseClass.updated_at,
        description = photoDataBaseClass.description,
        liked_by_user = photoDataBaseClass.liked_by_user,
        likes = photoDataBaseClass.likes,
        urls = photoDataBaseClass.urls,
        user = userDataBaseToUser(userDataBaseClass)
    )
}

fun collectionDataBaseClassToCollection(
    collectionDataBaseClass: CollectionDataBaseClass,
    userDataBaseClass: UserDataBaseClass
): Collection {
    return Collection(
        id = collectionDataBaseClass.id_collection,
        title = collectionDataBaseClass.title,
        description = collectionDataBaseClass.description,
        published_at = collectionDataBaseClass.published_at,
        last_collected_at = collectionDataBaseClass.last_collected_at,
        updated_at = collectionDataBaseClass.updated_at,
        total_photos = collectionDataBaseClass.total_photos,
        user = userDataBaseToUser(userDataBaseClass),
        cover_photo = Photo(
            id = collectionDataBaseClass.id_collection_photo ?: "",
            created_at = collectionDataBaseClass.createdat_collection_photo ?: "",
            updated_at = collectionDataBaseClass.updatedat_collection_photo ?: "",
            likes = collectionDataBaseClass.likes_collection_photo ?: "",
            liked_by_user = collectionDataBaseClass.likes_by_user_collection_photo ?: false,
            description = collectionDataBaseClass.description_collection_photo,
            user = userDataBaseToUser(userDataBaseClass),
            urls = collectionDataBaseClass.urls ?: Urls("", "")
        )
    )
}

fun collectionToUserWithCollection(collection: Collection): UserWithCollectionRelations {
    return UserWithCollectionRelations(
        userToUserDatabaseClass1(collection.user),
        listOf(collectionToCollectionDatabaseClass(collection))
    )
}

fun userWithCollectionToCollection(userWithCollectionRelations: UserWithCollectionRelations): List<Collection> {
    return userWithCollectionRelations.collection.map {
        Collection(
            id = it.id_collection,
            description = it.description,
            title = it.title,
            total_photos = it.total_photos,
            last_collected_at = it.last_collected_at,
            updated_at = it.updated_at,
            published_at = it.published_at,
            user = userDataBase1ToUser(userWithCollectionRelations.user),
            cover_photo = Photo(
                id = it.id_collection_photo ?: "",
                updated_at = it.updatedat_collection_photo ?: "",
                created_at = it.createdat_collection_photo ?: "",
                liked_by_user = it.likes_by_user_collection_photo ?: false,
                likes = it.likes_collection_photo ?: "",
                description = it.description_collection_photo,
                urls = it.urls ?: Urls("", ""),
                user = userDataBase1ToUser(userWithCollectionRelations.user)
            )
        )
    }
}