package com.example.unsplash.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.unsplash.R
import com.example.unsplash.adapter.PhotoPagingAdapter
import com.example.unsplash.adapter.UnsplashLoaderStateAdapter
import com.example.unsplash.app.App
import com.example.unsplash.data.Photo
import com.example.unsplash.utils.ReloadListener
import kotlinx.android.synthetic.main.lenta_layout.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Collections.emptyList
import javax.inject.Inject

class LentaFragment : Fragment(R.layout.lenta_layout) {

    @Inject
    lateinit var viewModel: LentaViewModel
    private var photoListAdapter: PhotoPagingAdapter? = null
    private var value: List<Photo> = emptyList()
    private var query: String = ""

    private val listener: ReloadListener?
        get() = parentFragment?.let { it as ReloadListener }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().applicationContext as App).component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photoListAdapter = PhotoPagingAdapter({ position -> showDetailPhotoFragment(position) })
        photoList.adapter = photoListAdapter?.withLoadStateHeaderAndFooter(
            header = UnsplashLoaderStateAdapter(),
            footer = UnsplashLoaderStateAdapter()
        )
        photoList.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        photoList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.HORIZONTAL
            )
        )
        photoList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getPhotoFromNetworkAndDB().collectLatest {
                photoListAdapter?.submitData(it)
            }
        }
        viewModel.list.observe(viewLifecycleOwner) {
            value += it
        }
        searchPhoto()
        photoListAdapter?.addLoadStateListener { state ->
            val error =
                (state.refresh == LoadState.Error(Throwable())) || (state.append == LoadState.Error(
                    Throwable()
                ))
            photoList.isVisible = (state.refresh != LoadState.Loading) && !error
            lentaProgress.isVisible = state.refresh == LoadState.Loading
            lentaError.isVisible = error
        }
    }

    //Search photo
    private fun searchPhoto() {
        val search = lentaTopAppBar.menu.findItem(R.id.search)
        (search.actionView as android.widget.SearchView).setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newText: String?): Boolean {
                if (newText != null) {
                    query = newText
                } else {
                    query = ""
                }
                //search photo function
                viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                    viewModel.searchPhotoData(query).collectLatest {
                        photoListAdapter?.submitData(it)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun showDetailPhotoFragment(position: Int) {
        listener?.onReloadListener(value[position])
    }

    override fun onDestroy() {
        super.onDestroy()
        photoListAdapter = null
    }
}