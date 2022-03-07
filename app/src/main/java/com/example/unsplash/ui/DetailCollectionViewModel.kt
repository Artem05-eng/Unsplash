package com.example.unsplash.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unsplash.data.Photo
import com.example.unsplash.data.Repository
import javax.inject.Inject

class DetailCollectionViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {

    private var bufferListPhotoMutable = MutableLiveData<List<Photo>>()
    private var throwableMutable = MutableLiveData<Throwable?>(null)

    val bufferListPhoto: LiveData<List<Photo>>
        get() = bufferListPhotoMutable
    val throwable: LiveData<Throwable?>
        get() = throwableMutable

    fun getCollectionPhotos(id: String) {
        repository.getCollectionPhotos(
            id,
            { s -> bufferListPhotoMutable.postValue(s) },
            { error -> throwableMutable.postValue(error) }
        )
    }
}