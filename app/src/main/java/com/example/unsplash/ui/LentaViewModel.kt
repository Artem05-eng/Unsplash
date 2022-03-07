package com.example.unsplash.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.unsplash.converters.userWithPhotoToPhoto
import com.example.unsplash.data.Photo
import com.example.unsplash.data.PhotoDataSource
import com.example.unsplash.data.SearchPhotoDataSource
import com.example.unsplash.database.UnsplashDatabase
import com.example.unsplash.mediator.UnsplashPhotoMediator
import com.example.unsplash.network.Api
import com.example.unsplash.network.Networking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LentaViewModel @Inject constructor(
    private val network: Api,
    private val db: UnsplashDatabase
) : ViewModel() {

    private val listMutable = MutableLiveData<List<Photo>>()
    val list: LiveData<List<Photo>>
        get() = listMutable

    fun getPhotoData(): Flow<PagingData<Photo>> {
        return Pager(config = PagingConfig(Networking.pageSize, prefetchDistance = 2),
            pagingSourceFactory = { PhotoDataSource(network, listMutable) })
            .flow
            .cachedIn(viewModelScope)
    }


    fun searchPhotoData(query: String): Flow<PagingData<Photo>> {
        return Pager(config = PagingConfig(Networking.pageSize, prefetchDistance = 2),
            pagingSourceFactory = { SearchPhotoDataSource(query, listMutable, network) })
            .flow
            .cachedIn(viewModelScope)
    }

    fun getPhotoFromNetworkAndDB(): Flow<PagingData<Photo>> {
        return getPhotoFromNetworkDB().cachedIn(viewModelScope)
    }


    @OptIn(ExperimentalPagingApi::class)
    fun getPhotoFromNetworkDB(): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(Networking.pageSize, enablePlaceholders = false),
            remoteMediator = UnsplashPhotoMediator(listMutable, db, network)
        ) {
            db.usersDao().getPagingUsersWithPhoto()
        }.flow.map { pagingData ->
            pagingData.map {
                userWithPhotoToPhoto(it).last()
            }
        }
    }

}