package com.example.unsplash.mediator

import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.unsplash.converters.photoToUserWithPhotos
import com.example.unsplash.data.Photo
import com.example.unsplash.data.UserWithPhotoRelations
import com.example.unsplash.database.UnsplashDatabase
import com.example.unsplash.network.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

private const val initailPage: Int = 1

@ExperimentalPagingApi
class UnsplashPhotoMediator(
    private val list: MutableLiveData<List<Photo>>,
    private val db: UnsplashDatabase,
    private val network: Api
) : RemoteMediator<Int, UserWithPhotoRelations>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserWithPhotoRelations>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val photo = getClosestPhoto(state)
                photo?.photo?.last()?.next_key?.minus(1) ?: initailPage
            }
            LoadType.PREPEND -> {
                val photo = getPhotoForFirstItem(state)
                val prev_key = photo?.photo?.first()?.prev_key ?: return MediatorResult.Success(
                    endOfPaginationReached = photo != null
                )
                prev_key
            }
            LoadType.APPEND -> {
                val photo = getPhotoForLastItem(state)
                val next_key = photo?.photo?.last()?.next_key ?: return MediatorResult.Success(
                    endOfPaginationReached = photo != null
                )
                next_key
            }
        }
        try {
            val photos = withContext(Dispatchers.IO) {
                network.getPhotos("popular", page).execute().body() ?: emptyList()
            }
            list.postValue(photos)
            val endOfPaginationReached = photos.isEmpty()
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.photosDao().clearPhotos()
                    db.usersDao().clearUsers()
                }
                val prevKey = if (page == initailPage) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val photosDB = photos.map { photo ->
                    photoToUserWithPhotos(photo).apply {
                        this.photo.forEach {
                            it.prev_key = prevKey
                            it.next_key = nextKey
                        }
                    }
                }
                photosDB.forEach {
                    db.usersDao().insertUsers(listOf(it.user))
                    db.photosDao().insertPhotos(it.photo)
                }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getPhotoForLastItem(state: PagingState<Int, UserWithPhotoRelations>): UserWithPhotoRelations? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()?.let { userWithPhotoRelation ->
                db.usersDao().getUserWithPhotoRelation(userWithPhotoRelation?.photo.last().id_photo)
            }
    }

    private suspend fun getPhotoForFirstItem(state: PagingState<Int, UserWithPhotoRelations>): UserWithPhotoRelations? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()?.let { userWithPhotoRelation ->
                db.usersDao()
                    .getUserWithPhotoRelation(userWithPhotoRelation?.photo.first().id_photo)
            }
    }

    private suspend fun getClosestPhoto(state: PagingState<Int, UserWithPhotoRelations>): UserWithPhotoRelations? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.photo?.last()?.id_photo?.let { photoId ->
                db.usersDao().getUserWithPhotoRelation(photoId)
            }
        }
    }
}