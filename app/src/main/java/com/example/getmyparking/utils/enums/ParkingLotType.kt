package com.example.getmyparking.utils.enums

import com.google.gson.annotations.SerializedName

enum class ParkingLotType{
    @SerializedName("CAR")
    CAR,
    @SerializedName("BIKE")
    BIKE,
    @SerializedName("COMMERCIAL")
    COMMERCIAL
}