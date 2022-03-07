package com.example.unsplash.utils

import com.example.unsplash.data.Collection
import com.example.unsplash.data.Photo

interface ButtonListener {
    fun onButtonListener(flag: Boolean)
}

interface LogoutListener {
    fun onLogoutListener(flag: Boolean)
}

interface ReloadListener {
    fun onReloadListener(photo: Photo)

    fun onReloadListenerCollection(collection: Collection)

    fun onReloadListenerCollectionDetail(photo: Photo)
}