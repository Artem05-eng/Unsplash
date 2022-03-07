package com.example.unsplash.ui

import android.Manifest
import android.app.DownloadManager
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.unsplash.R
import com.example.unsplash.data.Collection
import com.example.unsplash.data.Photo
import com.example.unsplash.network.Networking
import com.example.unsplash.utils.haveQ
import com.example.unsplash.utils.withArguments
import kotlinx.android.synthetic.main.card_collection_photo.*
import kotlinx.android.synthetic.main.photo_layout.*
import com.example.unsplash.MainActivity

import androidx.annotation.ContentView
import androidx.core.view.isVisible
import com.example.unsplash.app.App
import com.example.unsplash.services.DownloadService
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class DetailPhotoFragment : Fragment(R.layout.photo_layout) {

    @Inject
    lateinit var viewModel: DetailPhotoViewModel
    private var downloadID: Long = 0
    private var uriString = ""
    lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    private val downloadManager by lazy {
        requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    companion object {
        const val DATA_KEY = "data_key"
        const val DATA_SERVICE = "data_service"
        const val DATA_URI = "data_uri"
        const val CALL_REQUEST_CODE = 3456
        fun newInstance(photo: Photo): DetailPhotoFragment {
            return DetailPhotoFragment().withArguments {
                putParcelable(DATA_KEY, photo)
            }
        }

        private val PERMISSIONS = listOfNotNull(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE.takeIf {
                Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
            }
        )
    }

    private val onDownloadComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadID == id) {
                Snackbar.make(
                    photoInfoContainer,
                    "Загрузка выполнена успешно!",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("ОТКРЫТЬ") {
                        // Responds to click on the action
                        val imageIntent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(uriString)
                            type = "image/*"
                        }
                        if (imageIntent.resolveActivity(requireContext().packageManager) != null) {
                            startActivity(imageIntent)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "No component to handle intent!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().applicationContext as App).component.inject(this)
        requireContext().registerReceiver(
            onDownloadComplete,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionToGrantedMap: Map<String, Boolean> ->
            if (permissionToGrantedMap.values.all { it }) {
                Toast.makeText(requireContext(), "Разрешение включено!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Необходимо разрешение для действия!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        var photo = arguments?.getParcelable<Photo>(DATA_KEY) as Photo
        viewModel.getPhoto(photo.id)
        bind(photo)
        like.setOnClickListener {
            if (photo.liked_by_user != true) {
                viewModel.likePhoto(photo.id!!)
            } else {
                viewModel.unlikePhoto(photo.id!!)
            }
        }
        share.setOnClickListener {
            shareLinkPhoto(photo.urls.regular)
        }
        downloadImage.setOnClickListener {
            //download image
            if (hasPermission().not()) {
                requestPermissions()
            } else {
                //function
                download(photo.urls.regular, photo.id)
                val downloadIntent = Intent(requireContext(), DownloadService::class.java)
                    .putExtra(DATA_SERVICE, downloadID)
                    .putExtra(DATA_URI, uriString)
                requireContext().startService(downloadIntent)
            }
        }
        viewModel.bufferPhoto.observe(viewLifecycleOwner) {
            photo = it
            bind(it)
        }
        viewModel.throwable.observe(viewLifecycleOwner) {
            photoDetailError.isVisible = it != null
        }
    }

    private fun hasPermission(): Boolean {
        return PERMISSIONS.all {
            ActivityCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        requestPermissionLauncher.launch(PERMISSIONS.toTypedArray())
    }

    private fun bind(photo: Photo) {

        Glide.with(requireContext())
            .load(photo.urls.regular)
            .placeholder(R.drawable.ic_placeholder)
            .into(photoImageDetail)

        Glide.with(requireContext())
            .load(photo.user?.profile_image?.medium)
            .placeholder(R.drawable.ic_baseline_account_circle_24)
            .into(imageProfileAvatarPhotoDetail)

        nameProfilePhotoDetail.text = photo.user?.first_name + " " + photo.user?.last_name
        tagProfilePhotoDetail.text = "@" + photo.user?.username
        likeCount.text = photo.likes
        photoTopAppBar.title = photo.description

        if (photo.liked_by_user == true) {
            Glide.with(requireContext())
                .load(R.drawable.ic_favorite_24)
                .into(like)
        } else {
            Glide.with(requireContext())
                .load(R.drawable.ic_favorite_border_24)
                .into(like)
        }
    }

    private fun shareLinkPhoto(link: String) {
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.putExtra(Intent.EXTRA_TEXT, link)
        sendIntent.type = "text/plain"
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun download(url: String, name: String) {
        val volume = if (haveQ()) {
            MediaStore.VOLUME_EXTERNAL_PRIMARY
        } else {
            MediaStore.VOLUME_EXTERNAL
        }
        val imageCollectionUri = MediaStore.Images.Media.getContentUri(volume)
        val imageDetail = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/*")
        }
        val uri = requireContext().contentResolver.insert(imageCollectionUri, imageDetail)
        uriString = uri.toString()
        val request = DownloadManager.Request(Uri.parse(url))
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setTitle(name)
            .setDescription("Downloading image")
            .addRequestHeader("Authorization", "Bearer ${Networking.accessToken}")
            .setDestinationInExternalFilesDir(requireContext(), imageCollectionUri.toString(), name)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
            .setRequiresCharging(false)
            .setVisibleInDownloadsUi(false)

        downloadID = downloadManager.enqueue(request)
    }

    override fun onDestroy() {
        super.onDestroy()
        requireContext().unregisterReceiver(onDownloadComplete)
    }
}