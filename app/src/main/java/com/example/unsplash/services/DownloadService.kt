package com.example.unsplash.services

import android.app.DownloadManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.unsplash.data.NotificationObject
import com.example.unsplash.ui.DetailPhotoFragment
import kotlinx.coroutines.*

class DownloadService : Service() {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val downloadManager by lazy {
        this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    companion object {
        const val NOTIFICATION_ID = 4213
        const val REQUEST_CODE = 123
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        coroutineScope.launch {
            val downloadId = intent?.getLongExtra(DetailPhotoFragment.DATA_SERVICE, 0)
            val uriString = intent?.getStringExtra(DetailPhotoFragment.DATA_URI)
            if (downloadId != null && uriString != null) {
                loading(downloadId, uriString)
            }
            stopSelf()
        }
        return START_REDELIVER_INTENT
    }

    private fun loading(id: Long, uriString: String) {
        var finishDownload = false
        var progress: Int = 0
        while (!finishDownload) {
            val cursor = downloadManager.query(DownloadManager.Query().setFilterById(id))
            if (cursor.moveToFirst()) {
                var status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                when (status) {
                    DownloadManager.STATUS_FAILED -> {
                        val notification =
                            NotificationCompat.Builder(this, NotificationObject.MESSAGE_CHANNEL_ID)
                                .setContentTitle("Downloading Image")
                                .setContentText("Download failed!")
                                .setSmallIcon(android.R.drawable.stat_notify_error)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .build()
                        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification)
                        finishDownload = true
                    }
                    DownloadManager.STATUS_PAUSED -> {}
                    DownloadManager.STATUS_PENDING -> {}
                    DownloadManager.STATUS_RUNNING -> {
                        var total: Long =
                            cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                        if (total >= 0) {
                            var downloaded = cursor.getLong(
                                cursor.getColumnIndex(
                                    DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR
                                )
                            )
                            progress = ((downloaded * 100) / total).toInt()
                            val notification = NotificationCompat.Builder(
                                this,
                                NotificationObject.MESSAGE_CHANNEL_ID
                            )
                                .setContentTitle("Downloading Image")
                                .setContentText("Download running!")
                                .setSmallIcon(android.R.drawable.stat_sys_download)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setProgress(100, progress, false)
                                .setOnlyAlertOnce(true)
                                .build()
                            NotificationManagerCompat.from(this)
                                .notify(NOTIFICATION_ID, notification)
                        }
                    }
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(uriString)
                        }
                        val notification =
                            NotificationCompat.Builder(this, NotificationObject.MESSAGE_CHANNEL_ID)
                                .setContentTitle("Downloading Image")
                                .setContentText("Download successful!")
                                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setContentIntent(
                                    PendingIntent.getActivity(
                                        this,
                                        REQUEST_CODE,
                                        intent,
                                        PendingIntent.FLAG_UPDATE_CURRENT
                                    )
                                )
                                .build()
                        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification)
                        progress = 100
                        finishDownload = true
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}