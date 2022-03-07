package com.example.unsplash.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.unsplash.R
import com.example.unsplash.data.Photo
import com.example.unsplash.utils.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.card_collection_photo.*
import java.util.Collections.emptyList

class PhotoAdapter(
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<PhotoAdapter.PhotoHolder>() {

    private var photos: List<Photo> = emptyList()

    fun updatePhotoList(newPhotoList: List<Photo>) {
        photos = newPhotoList
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PhotoHolder {
        return PhotoHolder(p0.inflate(R.layout.card_collection_photo), onItemClicked)
    }

    override fun onBindViewHolder(p0: PhotoHolder, p1: Int) {
        val rep = photos[p1]
        p0.onBind(rep)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    class PhotoHolder(
        override val containerView: View?,
        private val onItemClicked: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(containerView!!), LayoutContainer {
        init {
            containerView!!.setOnClickListener {
                onItemClicked(adapterPosition)
            }
        }

        fun onBind(photo: Photo) {
            //Binding data-class Photo
            nameProfilePhotoPhotoCollection.text =
                photo.user?.first_name + " " + photo.user?.last_name
            tagProfilePhotoPhotoCollection.text = "@ " + photo.user?.username
            likeCountPhotoCollection.text = photo.likes

            if (photo.liked_by_user == true) {
                Glide.with(itemView)
                    .load(R.drawable.ic_favorite_24)
                    .into(likePhotoCollection)
            } else {
                Glide.with(itemView)
                    .load(R.drawable.ic_favorite_border_24)
                    .into(likePhotoCollection)
            }

            Glide.with(itemView)
                .load(photo.urls.regular)
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .into(photoImagePhotoCollection)

            Glide.with(itemView)
                .load(photo.user?.profile_image?.medium)
                .placeholder(R.drawable.ic_placeholder)
                .into(imageProfileAvatarPhotoPhotoCollection)
        }
    }
}