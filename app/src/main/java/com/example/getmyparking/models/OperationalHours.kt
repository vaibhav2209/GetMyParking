package com.example.getmyparking.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OperationalHours(
    val closeTime: String,
    val day: Int,
    @ColumnInfo(name = "operationalHourId")
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "operationalHourLastUpdatedBy")
    val lastUpdateBy: String,
    val openTime: String,
    val parkingId: Int
)