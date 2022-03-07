package com.example.unsplash.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.unsplash.converters.userWithCollectionToCollection
import com.example.unsplash.data.Collection
import com.example.unsplash.data.CollectionDataSource
import com.example.unsplash.database.UnsplashDatabase
import com.example.unsplash.mediator.UnsplashCollectionMediator
import com.example.unsplash.network.Api
import com.example.unsplash.network.Networking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CollectionViewModel @Inject constructor(
    private val network: Api,
    private val db: UnsplashDatabase
) : ViewModel() {

    private val listMutable = MutableLiveData<List<Collection>>()
    val list: LiveData<List<Collection>>
        get() = listMutable


    fun getCollectionData(): Flow<PagingData<Collection>> {
        return Pager(config = PagingConfig(pageSize = Networking.pageSize, prefetchDistance = 2),
            pagingSourceFactory = { CollectionDataSource(listMutable, network) })
            .flow
            .cachedIn(viewModelScope)
    }

    fun getCollectionFromNetworkDB(): Flow<PagingData<Collection>> {
        return getCollectionsFromNetworkandDB().cachedIn(viewModelScope)
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getCollectionsFromNetworkandDB(): Flow<PagingData<Collection>> {
        return Pager(
            config = PagingConfig(Networking.pageSize, enablePlaceholders = false),
            remoteMediator = UnsplashCollectionMediator(listMutable, db, network)
        ) {
            db.userCollectionDao().getPagingUsersWithCollection()
        }.flow.map { pagingData ->
            pagingData.map {
                userWithCollectionToCollection(it).last()
            }
        }
    }

}