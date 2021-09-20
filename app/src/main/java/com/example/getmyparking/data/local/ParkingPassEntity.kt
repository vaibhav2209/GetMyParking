package com.example.getmyparking.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.getmyparking.utils.enums.PassType


data class ParkingPassEntity(
//    @PrimaryKey
//    @ColumnInfo(name = "ParkingPassId")
    val id :Int,
    val type: PassType?,
    val accessTech: String?,
    val price: Int?,
    val parkingLotId: Int?,
//    @ColumnInfo(name = "ParkingPassLastUpdatedBy")
    val lastUpdateBy: String?
)