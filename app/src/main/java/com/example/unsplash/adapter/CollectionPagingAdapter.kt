package com.example.unsplash.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagedListAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.unsplash.R
import com.example.unsplash.data.Collection
import com.example.unsplash.data.Photo
import com.example.unsplash.utils.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.card_collection.*

class CollectionPagingAdapter(
    private val onItemClicked: (position: Int) -> Unit
) : PagingDataAdapter<Collection, CollectionPagingAdapter.CollectionHolder>(
    DiffUtilCallbackCollection()
) {

    class CollectionHolder(
        override val containerView: View,
        private val onItemClicked: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(containerView!!), LayoutContainer {
        init {
            containerView!!.setOnClickListener {
                onItemClicked(adapterPosition)
            }
        }

        fun onBind(collection: Collection) {
            //Binding data-class Photo
            containerView.isVisible = collection != null
            titleCardCollection.text = collection.title
            photoCount.text = collection.total_photos + " photos"
            nameProfileCollection.text =
                collection.user.first_name + " " + collection.user.last_name
            tagProfileCollection.text = "@" + collection.user.username

            Glide.with(itemView)
                .load(collection.user.profile_image?.medium)
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .into(imageProfileAvatarCollection)

            Glide.with(itemView)
                .load(collection.cover_photo?.urls?.regular)
                .placeholder(R.drawable.ic_placeholder)
                .into(imageCardCollection)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionHolder {
        return CollectionPagingAdapter.CollectionHolder(
            parent.inflate(R.layout.card_collection),
            onItemClicked
        )
    }

    override fun onBindViewHolder(holder: CollectionHolder, position: Int) {
        getItem(position)?.let { holder.onBind(it) }
    }
}

class DiffUtilCallbackCollection : DiffUtil.ItemCallback<Collection>() {
    override fun areItemsTheSame(oldItem: Collection, newItem: Collection): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Collection, newItem: Collection): Boolean {
        return oldItem.description == newItem.description &&
                oldItem.title == newItem.title &&
                oldItem.updated_at == newItem.updated_at
    }

}