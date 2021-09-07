package com.example.getmyparking.repository

import javax.crypto.SecretKey

interface HMACSHA256Generator {
    fun authorizeHeaderGenerator(key: String): String
    fun getCurrentTime(): String
}