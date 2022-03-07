package com.example.unsplash.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unsplash.R
import com.example.unsplash.adapter.CollectionAdapter
import com.example.unsplash.data.Collection
import com.example.unsplash.utils.withArguments
import kotlinx.android.synthetic.main.profile_collections.*

class ProfileCollectionsFragment : Fragment(R.layout.profile_collections) {

    private var collectionListAdapter: CollectionAdapter? = null

    companion object {
        const val DATA_KEY = "data_key"
        fun newInstance(collections: List<Collection>): ProfileCollectionsFragment {
            return ProfileCollectionsFragment().withArguments {
                putParcelableArray(DATA_KEY, collections.toTypedArray())
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val collections = arguments?.getParcelableArray(DATA_KEY)!!.toList() as List<Collection>
        collectionEmtyText.isVisible = (collections.isEmpty())
        collectionListAdapter = CollectionAdapter({ position -> })
        listProfileCollections.adapter = collectionListAdapter
        collectionListAdapter?.updateCollections(collections)
        listProfileCollections.layoutManager = LinearLayoutManager(requireContext())
        listProfileCollections.setHasFixedSize(true)
        listProfileCollections.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.HORIZONTAL
            )
        )
        listProfileCollections.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        collectionListAdapter?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        collectionListAdapter = null
    }
}