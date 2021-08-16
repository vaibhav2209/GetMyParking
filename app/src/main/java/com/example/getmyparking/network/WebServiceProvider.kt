package com.example.getmyparking.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebServiceProvider (context: Context) {

    private var requestQueue: RequestQueue = Volley.newRequestQueue(context)

    init {
        requestQueue.start()
    }


    fun <T> addToRequestQueue(request: Request<T>?) {
        requestQueue.add(request)
    }

}