package com.example.getmyparking.network

import com.android.volley.Request

enum class RequestType(val httpRequestType: Int) {
    GET (Request.Method.GET),
    POST(Request.Method.POST),
    DELETE(Request.Method.DELETE),
    PUT(Request.Method.PUT)
}