package com.example.getmyparking.utils.enums

import com.google.gson.annotations.SerializedName

enum class ParkingStructure {
    @SerializedName("OPEN_PARKING")
    OPEN_PARKING,
    @SerializedName("CLOSED_PARKING")
    CLOSED_PARKING
}