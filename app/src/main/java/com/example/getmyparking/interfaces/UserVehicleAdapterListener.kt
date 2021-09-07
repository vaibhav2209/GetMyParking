package com.example.getmyparking.interfaces

import com.example.getmyparking.models.Vehicle

interface UserVehicleAdapterListener {
    fun onVehicleClick(vehicle: Vehicle)
}