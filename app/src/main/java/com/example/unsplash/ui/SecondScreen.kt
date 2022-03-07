package com.example.unsplash.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.unsplash.R
import kotlinx.android.synthetic.main.second_screen.*

class SecondScreen : Fragment(R.layout.second_screen) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        screen_2_arrow_1.setOnClickListener {
            findNavController().popBackStack()
        }
        screen_2_arrow_2.setOnClickListener {
            val action = SecondScreenDirections.actionSecondScreenToThirdScreen()
            findNavController().navigate(action)
        }
    }
}