package com.example.unsplash.data

import net.openid.appauth.ResponseTypeValues

object AuthObject {
    const val AUTH_URI = "https://unsplash.com/oauth/authorize"
    const val TOKEN_URI = "https://unsplash.com/oauth/token"
    const val CLIENT_ID = "oqegAJbkJpGZ7Fo3aNH8HCbLwCIfKoBlR-1iZuLoZRU"
    const val CLIENT_SECRET = "-TBQipHmaenUsO0UaKhguE6phjov10woHog7OZ1LDrI"
    const val CALLBACK_URL = "ascent://com.example.unsplash"
    const val SCOPE =
        "public read_user write_user read_photos write_photos write_likes read_collections write_collections"
    const val RESPONSE_TYPE = ResponseTypeValues.CODE
}