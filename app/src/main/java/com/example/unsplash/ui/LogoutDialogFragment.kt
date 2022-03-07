package com.example.unsplash.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.unsplash.R
import com.example.unsplash.utils.ButtonListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LogoutDialogFragment : BottomSheetDialogFragment() {

    private val listener: ButtonListener?
        get() = parentFragment?.let { it as ButtonListener }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.alert_layout, container, false)
        val cancelButton = view.findViewById<Button>(R.id.cancelAlertButton)
        val okButton = view.findViewById<Button>(R.id.okAlertButton)
        cancelButton.setOnClickListener {
            dismiss()
        }
        okButton.setOnClickListener {
            listener?.onButtonListener(true)
            dismiss()
        }
        return view
    }

}