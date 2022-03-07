package com.example.unsplash.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.example.unsplash.R
import com.example.unsplash.app.App
import com.example.unsplash.data.Collection
import com.example.unsplash.data.Photo
import com.example.unsplash.data.User
import com.example.unsplash.utils.ButtonListener
import com.example.unsplash.utils.LogoutListener
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.profile_layout.*
import java.util.Collections.emptyList
import javax.inject.Inject

class ProfileFragment : Fragment(R.layout.profile_layout), ButtonListener {

    private val listener: LogoutListener?
        get() = parentFragment?.let { it as LogoutListener }

    @Inject
    lateinit var viewModel: ProfileViewModel

    //    private val viewModel: ProfileViewModel by viewModels()
    private var profile: User? = null
    private var favoritePhotos: List<Photo> = emptyList()
    private var photos: List<Photo> = emptyList()
    private var collections: List<Collection> = emptyList()
    private val fragment = LogoutDialogFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireContext().applicationContext as App).component.inject(this)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserProfile()
        viewModel.profile.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                profileName.text = result.first_name + " " + result.last_name
                profileLabel.text = "@" + result.username
                profileGeolocation.text = result.location
                Glide.with(this)
                    .load(result.profile_image?.large)
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .into(imageProfileAvatar)
                profile = result
                viewModel.getUsersPhoto(profile!!.username)
                viewModel.getFavoritePhotos(profile!!.username)
                viewModel.getUsersCollections(profile!!.username)
            }
        }

        childFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.profileContent, ProfileListFragment.newInstance(photos))
            .commit()

        profileTopAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logout -> {
                    logoutProfile()
                    true
                }
                else -> false
            }
        }

        viewModel.photos.observe(viewLifecycleOwner) {
            photos = it
        }
        viewModel.favoritePhoto.observe(viewLifecycleOwner) {
            favoritePhotos = it
        }
        viewModel.collections.observe(viewLifecycleOwner) {
            collections = it
        }

        profileTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(p0: TabLayout.Tab?) {
                when (p0?.position) {
                    //profilePhotoes
                    0 -> {
                        childFragmentManager.beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.profileContent, ProfileListFragment.newInstance(photos))
                            .commit()
                    }
                    //profileLikes
                    1 -> {
                        childFragmentManager.beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(
                                R.id.profileContent,
                                ProfileListFragment.newInstance(favoritePhotos)
                            )
                            .commit()
                    }
                    //profileCollections
                    2 -> {
                        childFragmentManager.beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(
                                R.id.profileContent,
                                ProfileCollectionsFragment.newInstance(collections)
                            )
                            .commit()
                    }
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {}

            override fun onTabReselected(p0: TabLayout.Tab?) {}

        })
    }

    private fun logoutProfile() {
        fragment.show(childFragmentManager, "")
    }

    override fun onButtonListener(flag: Boolean) {
        listener?.onLogoutListener(flag)
    }

}