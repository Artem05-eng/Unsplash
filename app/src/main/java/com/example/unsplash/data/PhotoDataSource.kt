package com.example.unsplash.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.unsplash.network.Api
import com.example.unsplash.network.Networking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException

class PhotoDataSource(
    private val network: Api,
    private val list: MutableLiveData<List<Photo>>
) : PagingSource<Int, Photo>() {

    private var value = emptyList<Photo>()

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        try {
            val nextPageNumber = params.key ?: 1
            val pageSize = params.loadSize.coerceAtMost(Networking.pageSize)
            var response = withContext(Dispatchers.IO) {
                network.getPhotos("popular", nextPageNumber).execute().body() ?: emptyList()
            }
            value = value + response
            list.postValue(value)
            return LoadResult.Page(
                data = response!!,
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (response!!.size < pageSize) null else nextPageNumber + 1
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        }
    }
}