package com.example.unsplash.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat

object NotificationObject {

    val MESSAGE_CHANNEL_ID = "messege"

    fun create(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createSimpleChannel(context)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createSimpleChannel(context: Context) {
        val name = "Download"
        val channelDescription = "download image"
        val priority = NotificationManager.IMPORTANCE_HIGH

        val channel = NotificationChannel(MESSAGE_CHANNEL_ID, name, priority).apply {
            description = channelDescription
        }
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }

}