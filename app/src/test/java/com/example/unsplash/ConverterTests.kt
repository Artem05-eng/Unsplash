package com.example.unsplash

import com.example.unsplash.converters.photoDataBaseClassToPhoto
import com.example.unsplash.converters.photoToPhotoDatabaseClass
import com.example.unsplash.converters.userDataBaseToUser
import com.example.unsplash.converters.userToUserDatabaseClass1
import com.example.unsplash.data.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TestConverters {

    val user = User(
        id = "11",
        first_name = "Artem",
        last_name = null,
        location = "Ufa",
        profile_image = null,
        total_collections = "1",
        total_photos = "22",
        total_likes = "24",
        updated_at = "3",
        username = "artem"
    )

    val userDataBaseClass = UserDataBaseClass(
        id_user = "11",
        first_name = "Artem",
        last_name = null,
        location = "Ufa",
        profile_image = null,
        total_collections = "1",
        total_photos = "22",
        total_likes = "24",
        updated_at = "3",
        username = "artem"
    )

    val photo = Photo(
        id = "1",
        created_at = "2",
        updated_at = "3",
        likes = "4",
        liked_by_user = true,
        description = null,
        urls = Urls("5.1", "5.2"),
        user = user
    )

    val photoDatabase = PhotoDataBaseClass(
        id_photo = "1",
        user_id = "11",
        created_at = "2",
        updated_at = "3",
        likes = "4",
        liked_by_user = true,
        description = null,
        urls = Urls("5.1", "5.2"),
        next_key = null,
        prev_key = null
    )

    @Test
    fun testConverterUserToUserDatabase() {
        val actual = userToUserDatabaseClass1(user)
        assertThat((actual), `is` (userDataBaseClass))
    }

    @Test
    fun testConverterUserDatabaseToUser() {
        val actual = userDataBaseToUser(userDataBaseClass)
        assertThat((actual), `is` (user))
    }

    @Test
    fun testConverterPhotoToPhotoDatabase() {
        val actual = photoToPhotoDatabaseClass(photo)
        assertThat((actual), `is` (photoDatabase))
    }

    @Test
    fun testConverterPhotoDatabaseToPhoto() {
        val actual = photoDataBaseClassToPhoto(photoDatabase, userDataBaseClass)
        assertThat((actual), `is` (photo))
    }
}