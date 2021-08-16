package com.example.getmyparking.models

data class ParkingLot(
    val parkingName:String,
    val openingHours:String,
    val pricing:String,
    val capacity:Int,
    val geoLocation: GeoLocation
)
