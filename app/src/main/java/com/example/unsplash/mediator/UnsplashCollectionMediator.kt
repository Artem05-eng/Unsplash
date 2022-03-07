package com.example.unsplash.mediator

import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.unsplash.converters.collectionToUserWithCollection
import com.example.unsplash.data.Collection
import com.example.unsplash.data.UserWithCollectionRelations
import com.example.unsplash.database.UnsplashDatabase
import com.example.unsplash.network.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

private const val initailPage: Int = 1

@ExperimentalPagingApi
class UnsplashCollectionMediator(
    private val list: MutableLiveData<List<Collection>>,
    private val db: UnsplashDatabase,
    private val network: Api
) : RemoteMediator<Int, UserWithCollectionRelations>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserWithCollectionRelations>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val collection = getClosestCollection(state)
                collection?.collection?.last()?.next_key?.minus(1) ?: initailPage
            }
            LoadType.PREPEND -> {
                val collection = getCollectionForFirstItem(state)
                val prev_key =
                    collection?.collection?.first()?.prev_key ?: return MediatorResult.Success(
                        endOfPaginationReached = collection != null
                    )
                prev_key
            }
            LoadType.APPEND -> {
                val collection = getCollectionForLastItem(state)
                val next_key =
                    collection?.collection?.last()?.next_key ?: return MediatorResult.Success(
                        endOfPaginationReached = collection != null
                    )
                next_key
            }
        }
        try {
            val collections = withContext(Dispatchers.IO) {
                network.getCollections(page).execute().body() ?: emptyList()
            }
            list.postValue(collections)
            val endOfPaginationReached = collections.isEmpty()
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.collectionsDao().clearCollections()
                    db.userCollectionDao().clearUsers()
                }
                val prevKey = if (page == initailPage) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val collectionsDB = collections.map { collection ->
                    collectionToUserWithCollection(collection).apply {
                        this.collection.forEach {
                            it.next_key = nextKey
                            it.prev_key = prevKey
                        }
                    }
                }
                collectionsDB.forEach {
                    db.userCollectionDao().insertUsers(listOf(it.user))
                    db.collectionsDao().insertCollections(it.collection)
                }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getCollectionForLastItem(state: PagingState<Int, UserWithCollectionRelations>): UserWithCollectionRelations? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()?.let { userWithCollectionRelation ->
                db.userCollectionDao()
                    .getUserWithCollectionRelation(userWithCollectionRelation?.collection?.last()?.id_collection)
            }
    }

    private suspend fun getCollectionForFirstItem(state: PagingState<Int, UserWithCollectionRelations>): UserWithCollectionRelations? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()?.let { userWithCollectionRelation ->
                db.userCollectionDao()
                    .getUserWithCollectionRelation(userWithCollectionRelation?.collection?.first().id_collection)
            }
    }

    private suspend fun getClosestCollection(state: PagingState<Int, UserWithCollectionRelations>): UserWithCollectionRelations? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.collection?.last()?.id_collection?.let { collectionId ->
                db.userCollectionDao().getUserWithCollectionRelation(collectionId)
            }
        }
    }
}