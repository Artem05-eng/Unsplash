package com.example.unsplash.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unsplash.data.Collection
import com.example.unsplash.data.Photo
import com.example.unsplash.data.Repository
import com.example.unsplash.data.User
import javax.inject.Inject

class ProfileViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    private var favoritePhotosMutable = MutableLiveData<List<Photo>>()
    private var photosMutable = MutableLiveData<List<Photo>>()
    private var favoriteCollectionsMutable = MutableLiveData<List<Collection>>()
    private var profileMutable = MutableLiveData<User>()
    private var throwableMutable = MutableLiveData<Throwable?>(null)

    val photos: LiveData<List<Photo>>
        get() = photosMutable
    val collections: LiveData<List<Collection>>
        get() = favoriteCollectionsMutable
    val profile: LiveData<User>
        get() = profileMutable
    val favoritePhoto: LiveData<List<Photo>>
        get() = favoritePhotosMutable
    val throwable: LiveData<Throwable?>
        get() = throwableMutable


    fun getUserProfile() {
        repository.getUserProfile(
            { s -> profileMutable.postValue(s) },
            { error -> throwableMutable.postValue(error) })
    }

    fun getFavoritePhotos(username: String) {
        repository.getFavoritePhotos(
            username,
            { s -> favoritePhotosMutable.postValue(s) },
            { error -> throwableMutable.postValue(error) })
    }

    fun getUsersCollections(username: String) {
        repository.getUsersCollections(
            username,
            { s -> favoriteCollectionsMutable.postValue(s) },
            { error -> throwableMutable.postValue(error) })
    }

    fun getUsersPhoto(username: String) {
        repository.getUsersPhotos(
            username,
            { s -> photosMutable.postValue(s) },
            { error -> throwableMutable.postValue(error) })
    }
}