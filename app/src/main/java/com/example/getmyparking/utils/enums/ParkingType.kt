package com.example.getmyparking.utils.enums

import com.google.gson.annotations.SerializedName

enum class ParkingType {
    @SerializedName("MULTILEVEL")
    MULTILEVEL,
    @SerializedName("ON_STREET")
    ON_STREET,
    @SerializedName("OFF_STREET")
    OFF_STREET
}