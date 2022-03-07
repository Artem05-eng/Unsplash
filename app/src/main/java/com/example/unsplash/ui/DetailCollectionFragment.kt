package com.example.unsplash.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unsplash.R
import com.example.unsplash.adapter.PhotoAdapter
import com.example.unsplash.app.App
import com.example.unsplash.data.Collection
import com.example.unsplash.data.Photo
import com.example.unsplash.utils.ReloadListener
import com.example.unsplash.utils.withArguments
import kotlinx.android.synthetic.main.collection_detail_layout.*
import java.util.Collections.emptyList
import javax.inject.Inject

class DetailCollectionFragment : Fragment(R.layout.collection_detail_layout) {

    private var photoAdapter: PhotoAdapter? = null
    @Inject
    lateinit var viewModel: DetailCollectionViewModel
    private var photos: List<Photo> = emptyList()

    private val listener: ReloadListener?
        get() = parentFragment?.let { it as ReloadListener }

    companion object {
        const val DATA_KEY = "data_key"
        fun newInstance(collection: Collection): DetailCollectionFragment {
            return DetailCollectionFragment().withArguments {
                putParcelable(DATA_KEY, collection)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().applicationContext as App).component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val collection = arguments?.getParcelable<Collection>(DATA_KEY) as Collection
        photoAdapter = PhotoAdapter({ position -> showDetailPhoto(position) })
        photoCollectionList.adapter = photoAdapter
        photoCollectionList.layoutManager = LinearLayoutManager(requireContext())
        photoCollectionList.setHasFixedSize(true)
        photoCollectionList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.HORIZONTAL
            )
        )
        photoCollectionList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        viewModel.getCollectionPhotos(collection.id)
        collectionDetailTopAppBar.title = collection.title
        collectionName.text = collection.title
        collectionComment.text = collection.description
        collectionInfo.text = collection.total_photos + " by @" + collection.user.username
        viewModel.bufferListPhoto.observe(viewLifecycleOwner) {
            bind(it)
            photoAdapter?.notifyDataSetChanged()
        }
        viewModel.throwable.observe(viewLifecycleOwner) {
            collectionDetailError.isVisible = it != null
        }
    }

    private fun bind(photolist: List<Photo>) {
        photos = photolist
        photoAdapter?.updatePhotoList(photos)
        photoAdapter?.notifyDataSetChanged()
        photoCollectionList.scrollToPosition(0)
    }

    private fun showDetailPhoto(position: Int) {
        listener?.onReloadListenerCollectionDetail(photos[position])
    }

    override fun onDestroy() {
        super.onDestroy()
        photoAdapter = null
    }

}