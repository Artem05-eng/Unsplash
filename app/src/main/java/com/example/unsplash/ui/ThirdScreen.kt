package com.example.unsplash.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.unsplash.R
import kotlinx.android.synthetic.main.third_screen.*

class ThirdScreen : Fragment(R.layout.third_screen) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        screen_3_arrow_1.setOnClickListener {
            findNavController().popBackStack()
        }
        screen_3_arrow_2.setOnClickListener {
            val action = ThirdScreenDirections.actionThirdScreenToLoginFragment()
            findNavController().navigate(action)
        }
    }
}