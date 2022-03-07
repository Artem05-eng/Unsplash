package com.example.unsplash.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.unsplash.R
import com.example.unsplash.data.Collection
import com.example.unsplash.utils.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.card_collection.*
import java.util.Collections.emptyList

class CollectionAdapter(
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<CollectionAdapter.CollectionHolder>() {

    private var collections: List<Collection> = emptyList()

    fun updateCollections(newCollections: List<Collection>) {
        collections = newCollections
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CollectionHolder {
        return CollectionHolder(p0.inflate(R.layout.card_collection), onItemClicked)
    }

    override fun onBindViewHolder(p0: CollectionHolder, p1: Int) {
        val rep = collections[p1]
        p0.onBind(rep)
    }

    override fun getItemCount(): Int {
        return collections.size
    }

    class CollectionHolder(
        override val containerView: View?,
        private val onItemClicked: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(containerView!!), LayoutContainer {
        init {
            containerView!!.setOnClickListener {
                onItemClicked(adapterPosition)
            }
        }

        fun onBind(collection: Collection) {
            //Binding data-class Collection
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
}