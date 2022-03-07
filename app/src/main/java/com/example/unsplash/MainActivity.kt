package com.example.unsplash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val data = handleIntentData()
    }

    private fun handleIntentData(): String? {
        val result = intent?.data?.lastPathSegment
        return result
    }
}