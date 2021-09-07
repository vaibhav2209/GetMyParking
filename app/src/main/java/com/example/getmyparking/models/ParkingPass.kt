package com.example.getmyparking.models

import com.example.getmyparking.utils.enums.PassType

data class ParkingPass(
    val id :Int,
    val type: PassType,
    val accessTech: String,
    val price: Int,
    val parkingLotId: Int,
    val lastUpdateBy: String
)