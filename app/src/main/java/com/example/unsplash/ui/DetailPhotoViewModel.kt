package com.example.unsplash.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unsplash.data.Photo
import com.example.unsplash.data.Repository
import javax.inject.Inject

class DetailPhotoViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private var bufferPhotoMutable = MutableLiveData<Photo>()
    private var photoIdMutable = MutableLiveData<Photo>()
    private var throwableMutable = MutableLiveData<Throwable?>(null)

    val bufferPhoto: LiveData<Photo>
        get() = bufferPhotoMutable

    val throwable: LiveData<Throwable?>
        get() = throwableMutable

    fun likePhoto(id: String) {
        repository.likePhoto(
            id,
            { s -> bufferPhotoMutable.postValue(s) },
            { error -> throwableMutable.postValue(error) })
    }

    fun unlikePhoto(id: String) {
        repository.unlikePhoto(
            id,
            { s -> bufferPhotoMutable.postValue(s) },
            { error -> throwableMutable.postValue(error) })
    }

    fun getPhoto(id: String) {
        repository.getPhoto(
            id,
            { s -> photoIdMutable.postValue(s) },
            { error -> throwableMutable.postValue(error) })
    }

}