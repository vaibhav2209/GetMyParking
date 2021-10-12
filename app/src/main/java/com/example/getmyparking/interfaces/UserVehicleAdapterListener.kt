package com.example.getmyparking.interfaces

import com.example.getmyparking.data.local.CarEntity
import com.example.getmyparking.models.Vehicle

interface UserVehicleAdapterListener {
    fun onVehicleClick(carEntity: CarEntity)
}