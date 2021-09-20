package com.example.getmyparking.data.local

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


data class OperationalHoursEntity(
    val closeTime: String?,
    val day: Int?,
    val id: Int,
    val lastUpdateBy: String?,
    val openTime: String?,
    val parkingId: Int?
)