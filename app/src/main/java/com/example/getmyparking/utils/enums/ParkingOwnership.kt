package com.example.getmyparking.utils.enums

import com.google.gson.annotations.SerializedName

enum class ParkingOwnership {
    @SerializedName("GOVERNMENT")
    GOVERNMENT,
    @SerializedName("PRIVATE")
    PRIVATE
}