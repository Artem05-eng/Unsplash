package com.example.unsplash.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unsplash.R
import com.example.unsplash.adapter.CollectionPagingAdapter
import com.example.unsplash.adapter.UnsplashLoaderStateAdapter
import com.example.unsplash.app.App
import com.example.unsplash.data.Collection
import com.example.unsplash.utils.ReloadListener
import kotlinx.android.synthetic.main.collections_layout.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Collections.emptyList
import javax.inject.Inject

class CollectionFragment : Fragment(R.layout.collections_layout) {

    private val listener: ReloadListener?
        get() = parentFragment?.let { it as ReloadListener }

    @Inject
    lateinit var viewModel: CollectionViewModel
    private var collectionListAdapter: CollectionPagingAdapter? = null
    private var value: List<Collection> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().applicationContext as App).component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectionListAdapter =
            CollectionPagingAdapter({ position -> showDetailCollectionFragment(position) })
        collectionList.adapter = collectionListAdapter?.withLoadStateHeaderAndFooter(
            header = UnsplashLoaderStateAdapter(),
            footer = UnsplashLoaderStateAdapter()
        )
        collectionList.layoutManager = LinearLayoutManager(requireContext())
        collectionList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.HORIZONTAL
            )
        )
        collectionList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getCollectionFromNetworkDB().collectLatest {
                collectionListAdapter?.submitData(it)
            }
        }
        viewModel.list.observe(viewLifecycleOwner) {
            value += it
        }
        collectionListAdapter?.addLoadStateListener { state ->
            val error =
                (state.refresh == LoadState.Error(Throwable())) || (state.append == LoadState.Error(
                    Throwable()
                ))
            collectionList.isVisible = (state.refresh != LoadState.Loading) && !error
            collectionListProgress.isVisible = state.refresh == LoadState.Loading
            collectionListError.isVisible = error
        }
    }

    private fun showDetailCollectionFragment(position: Int) {
        //Navigation to Detail Collection Fragment
        listener?.onReloadListenerCollection(value[position])
    }

    override fun onDestroy() {
        super.onDestroy()
        collectionListAdapter = null
    }
}