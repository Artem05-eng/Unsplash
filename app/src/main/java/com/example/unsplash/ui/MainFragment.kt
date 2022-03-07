package com.example.unsplash.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.unsplash.R
import com.example.unsplash.data.Collection
import com.example.unsplash.data.Photo
import com.example.unsplash.utils.LogoutListener
import com.example.unsplash.utils.ReloadListener
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment(R.layout.main_fragment), LogoutListener, ReloadListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction().replace(R.id.container_1, LentaFragment()).commit()
        bottom_navigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    childFragmentManager.beginTransaction()
                        .replace(R.id.container_1, LentaFragment()).commit()
                    true
                }
                R.id.page_2 -> {
                    childFragmentManager.beginTransaction()
                        .replace(R.id.container_1, CollectionFragment()).commit()
                    true
                }
                R.id.page_3 -> {
                    childFragmentManager.beginTransaction()
                        .replace(R.id.container_1, ProfileFragment()).commit()
                    true
                }
                else -> false
            }
        }
    }

    override fun onLogoutListener(flag: Boolean) {
        if (flag == true) {
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToLoginFragment(
                    flag
                )
            )
        }
    }

    //Show detail photo info
    override fun onReloadListener(photo: Photo) {
        childFragmentManager.beginTransaction()
            .replace(R.id.container_1, DetailPhotoFragment.newInstance(photo))
            .addToBackStack("Detail Photo").commit()
    }

    override fun onReloadListenerCollection(collection: Collection) {
        childFragmentManager.beginTransaction()
            .replace(R.id.container_1, DetailCollectionFragment.newInstance(collection))
            .addToBackStack("Detail Collection").commit()
    }

    override fun onReloadListenerCollectionDetail(photo: Photo) {
        childFragmentManager.beginTransaction()
            .replace(R.id.container_1, DetailPhotoFragment.newInstance(photo))
            .addToBackStack("Detail Collection Photo").commit()
    }


}