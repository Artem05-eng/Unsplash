package com.example.unsplash.data

import android.accounts.NetworkErrorException
import android.util.Log
import com.example.unsplash.network.Api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    val network: Api
) : Repository {

    override fun getUserProfile(callback: (User) -> Unit, callbackError: (Throwable?) -> Unit) {
        network.getProfile().enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        callback(user)
                        callbackError(null)
                    }
                } else {
                    Log.d("server", "JSON exception")
                    callbackError(NetworkErrorException())
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("server", t.message, t)
                callbackError(NetworkErrorException())
            }
        })
    }

    override fun getFavoritePhotos(
        username: String,
        callback: (List<Photo>) -> Unit,
        callbackError: (Throwable?) -> Unit
    ) {
        network.getFavoritePhotos(username).enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                if (response.isSuccessful) {
                    val photoes = response.body() ?: Collections.emptyList()
                    callback(photoes)
                    callbackError(null)
                } else {
                    Log.d("server", "JSON exception")
                    callbackError(NetworkErrorException())
                }
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                Log.e("server", t.message, t)
                callbackError(NetworkErrorException())
            }
        })
    }

    override fun getUsersCollections(
        username: String,
        callback: (List<Collection>) -> Unit,
        callbackError: (Throwable?) -> Unit
    ) {
        network.getUsersCollections(username).enqueue(object : Callback<List<Collection>> {
            override fun onResponse(
                call: Call<List<Collection>>,
                response: Response<List<Collection>>
            ) {
                if (response.isSuccessful) {
                    val collections = response.body() ?: Collections.emptyList()
                    callback(collections)
                    callbackError(null)
                } else {
                    Log.d("server", "JSON exception")
                    callbackError(NetworkErrorException())
                }
            }

            override fun onFailure(call: Call<List<Collection>>, t: Throwable) {
                Log.e("server", t.message, t)
                callbackError(NetworkErrorException())
            }
        })
    }

    override fun likePhoto(
        id: String,
        callback: (Photo) -> Unit,
        callbackError: (Throwable?) -> Unit
    ) {
        network.likePhoto(id).enqueue(object : Callback<WrapperPhoto<Photo>> {
            override fun onResponse(
                p0: Call<WrapperPhoto<Photo>>,
                p1: Response<WrapperPhoto<Photo>>
            ) {
                if (p1.isSuccessful) {
                    val response = p1.body()
                    if (response != null) {
                        callback(response.photo)
                        callbackError(null)
                    }
                } else {
                    Log.d("server", "JSON exception")
                    callbackError(NetworkErrorException())
                }
            }

            override fun onFailure(p0: Call<WrapperPhoto<Photo>>, p1: Throwable) {
                Log.e("server", p1.message, p1)
                callbackError(NetworkErrorException())
            }
        })
    }

    override fun unlikePhoto(
        id: String,
        callback: (Photo) -> Unit,
        callbackError: (Throwable?) -> Unit
    ) {
        network.unlikePhoto(id).enqueue(object : Callback<WrapperPhoto<Photo>> {
            override fun onResponse(
                p0: Call<WrapperPhoto<Photo>>,
                p1: Response<WrapperPhoto<Photo>>
            ) {
                if (p1.isSuccessful) {
                    val response = p1.body()
                    if (response != null) {
                        callback(response.photo)
                        callbackError(null)
                    }
                } else {
                    Log.d("server", "JSON exception")
                    callbackError(NetworkErrorException())
                }
            }

            override fun onFailure(p0: Call<WrapperPhoto<Photo>>, p1: Throwable) {
                Log.e("server", p1.message, p1)
                callbackError(NetworkErrorException())
            }
        })
    }

    override fun searchCollection(query: String) {
        network.searchCollections(query).enqueue(object : Callback<List<Collection>> {
            override fun onResponse(p0: Call<List<Collection>>, p1: Response<List<Collection>>) {
                if (p1.isSuccessful) {
                    val collections = p1.body() ?: Collections.emptyList()
                } else {
                    Log.d("server", "JSON exception")
                }
            }

            override fun onFailure(p0: Call<List<Collection>>, p1: Throwable) {
                Log.e("server", p1.message, p1)
            }
        })
    }

    override fun getPhoto(
        id: String,
        callback: (Photo) -> Unit,
        callbackError: (Throwable?) -> Unit
    ) {
        network.getPhoto(id).enqueue(object : Callback<Photo> {
            override fun onResponse(p0: Call<Photo>, p1: Response<Photo>) {
                if (p1.isSuccessful) {
                    val response = p1.body()
                    if (response != null) {
                        callback(response)
                        callbackError(null)
                    }
                } else {
                    Log.d("server", "JSON exception")
                    callbackError(NetworkErrorException())
                }
            }

            override fun onFailure(p0: Call<Photo>, p1: Throwable) {
                Log.e("server", p1.message, p1)
                callbackError(NetworkErrorException())
            }
        })
    }

    override fun getCollection(id: String, callback: (Collection) -> Unit) {
        network.getCollection(id).enqueue(object : Callback<Collection> {
            override fun onResponse(p0: Call<Collection>, p1: Response<Collection>) {
                if (p1.isSuccessful) {
                    val response = p1.body()
                    if (response != null) {
                        callback(response)
                    }
                } else {
                    Log.d("server", "JSON exception")
                }
            }

            override fun onFailure(p0: Call<Collection>, p1: Throwable) {
                Log.e("server", p1.message, p1)
            }
        })
    }

    override fun getCollectionPhotos(
        id: String,
        callback: (List<Photo>) -> Unit,
        callbackError: (Throwable?) -> Unit
    ) {
        network.getCollectionPhotos(id).enqueue(object : Callback<List<Photo>> {
            override fun onResponse(p0: Call<List<Photo>>, p1: Response<List<Photo>>) {
                if (p1.isSuccessful) {
                    val photos = p1.body() ?: Collections.emptyList()
                    callback(photos)
                    callbackError(null)
                } else {
                    Log.d("server", "JSON exception")
                    callbackError(NetworkErrorException())
                }
            }

            override fun onFailure(p0: Call<List<Photo>>, p1: Throwable) {
                Log.e("server", p1.message, p1)
                callbackError(NetworkErrorException())
            }
        })
    }

    override fun getUsersPhotos(
        username: String,
        callback: (List<Photo>) -> Unit,
        callbackError: (Throwable?) -> Unit
    ) {
        network.getUsersPhoto(username).enqueue(object : Callback<List<Photo>> {
            override fun onResponse(p0: Call<List<Photo>>, p1: Response<List<Photo>>) {
                if (p1.isSuccessful) {
                    val photos = p1.body() ?: Collections.emptyList()
                    callback(photos)
                    callbackError(null)
                } else {
                    Log.d("server", "JSON exception")
                    callbackError(NetworkErrorException())
                }
            }

            override fun onFailure(p0: Call<List<Photo>>, p1: Throwable) {
                Log.e("server", p1.message, p1)
                callbackError(NetworkErrorException())
            }
        })
    }
}