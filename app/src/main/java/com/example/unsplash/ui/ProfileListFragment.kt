package com.example.unsplash.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unsplash.R
import com.example.unsplash.adapter.PhotoAdapter
import com.example.unsplash.data.Photo
import com.example.unsplash.utils.withArguments
import kotlinx.android.synthetic.main.profile_list.*

class ProfileListFragment : Fragment(R.layout.profile_list) {

    companion object {
        const val DATA_KEY = "data_key"
        fun newInstance(photos: List<Photo>): ProfileListFragment {
            return ProfileListFragment().withArguments {
                putParcelableArray(DATA_KEY, photos.toTypedArray())
            }
        }
    }

    private var photoListAdapter: PhotoAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val photos = arguments?.getParcelableArray(ProfileCollectionsFragment.DATA_KEY)!!
            .toList() as List<Photo>
        photoEmtyText.isVisible = (photos.isEmpty())
        photoListAdapter = PhotoAdapter({ position -> })
        listProfileContent.adapter = photoListAdapter
        photoListAdapter?.updatePhotoList(photos)
        listProfileContent.layoutManager = LinearLayoutManager(requireContext())
        listProfileContent.setHasFixedSize(true)
        listProfileContent.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.HORIZONTAL
            )
        )
        listProfileContent.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        photoListAdapter?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        photoListAdapter = null
    }
}