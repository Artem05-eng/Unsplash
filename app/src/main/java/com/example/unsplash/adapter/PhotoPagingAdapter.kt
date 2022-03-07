package com.example.unsplash.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.unsplash.R
import com.example.unsplash.data.Photo
import com.example.unsplash.utils.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.card_collection_photo.*

class PhotoPagingAdapter(
    private val onItemClicked: (position: Int) -> Unit
) : PagingDataAdapter<Photo, PhotoPagingAdapter.PhotoHolder>(DiffUtilCallback()) {

    class PhotoHolder(
        override val containerView: View,
        private val onItemClicked: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(containerView!!), LayoutContainer {
        init {
            containerView!!.setOnClickListener {
                onItemClicked(adapterPosition)
            }
        }

        fun onBind(photo: Photo) {
            //Binding data-class Photo
            containerView.isVisible = photo != null
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
                .placeholder(R.drawable.ic_placeholder_24)
                .into(photoImagePhotoCollection)

            Glide.with(itemView)
                .load(photo.user?.profile_image?.medium)
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .into(imageProfileAvatarPhotoPhotoCollection)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        return PhotoHolder(parent.inflate(R.layout.card_collection_photo), onItemClicked)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        getItem(position)?.let { holder.onBind(it) }
    }

}

class DiffUtilCallback : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.description == newItem.description &&
                oldItem.liked_by_user == newItem.liked_by_user &&
                oldItem.created_at == newItem.created_at &&
                oldItem.likes == newItem.likes &&
                oldItem.updated_at == newItem.updated_at
    }

}