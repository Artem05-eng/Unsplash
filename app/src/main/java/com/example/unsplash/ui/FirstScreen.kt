package com.example.unsplash.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.unsplash.R
import kotlinx.android.synthetic.main.first_screen.*

class FirstScreen : Fragment(R.layout.first_screen) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        screen_1_arrow_1.setOnClickListener {
            val action = FirstScreenDirections.actionFirstScreenToSecondScreen()
            findNavController().navigate(action)
        }
        screen_1_cancel.setOnClickListener {
            val action = FirstScreenDirections.actionFirstScreenToLoginFragment()
            findNavController().navigate(action)
        }
    }
}