package com.example.unsplash.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.unsplash.R
import com.example.unsplash.utils.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.load_state_layout.*

class UnsplashLoaderStateAdapter :
    LoadStateAdapter<UnsplashLoaderStateAdapter.UnsplashLoaderStateViewHolder>() {

    class UnsplashLoaderStateViewHolder(
        override val containerView: View?
    ) : RecyclerView.ViewHolder(containerView!!), LayoutContainer {
        fun bind(loadState: LoadState) {
            load_state_errorMessage.isVisible = loadState is LoadState.Error
            load_state_progress.isVisible = loadState is LoadState.Loading
        }
    }

    override fun onBindViewHolder(holder: UnsplashLoaderStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): UnsplashLoaderStateViewHolder {
        return UnsplashLoaderStateAdapter.UnsplashLoaderStateViewHolder(parent.inflate(R.layout.load_state_layout))
    }

}