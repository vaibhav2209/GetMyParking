package com.example.getmyparking.models

import com.example.getmyparking.others.enums.VehicleType

data class Vehicle(
    val vehicleModel: String,
    val vehicleNumber: String,
    val vehicleType:VehicleType
)
