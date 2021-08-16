package com.example.getmyparking.utils

import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.getmyparking.databinding.LayoutCustomProcessDialogBinding
import java.lang.ref.WeakReference


class CustomProcessDialog :DialogFragment() {

    var contextReference: WeakReference<Context>? = null
    var rootView: View? = null
    var message: String? = null
    var textViewMessage: TextView? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //rootView = inflater.inflate(R.layout,container)
        return rootView
    }

    private fun initUI() {
        /*textViewMessage = rootView?.findViewById(R.id.textViewMessage) as TextView
        id.message = arguments!!.getString("message", "Loading")
        textViewMessage.setText(id.message)*/
    }
}