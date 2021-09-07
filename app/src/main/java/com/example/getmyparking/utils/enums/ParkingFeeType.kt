package com.example.getmyparking.utils.enums

import com.google.gson.annotations.SerializedName

enum class ParkingFeeType {
    @SerializedName("DURATION_BASED_PRICING")
    DURATION_BASED_PRICING,
    @SerializedName("FIXED_PRICE")
    FIXED_PRICE,
    @SerializedName("FREE")
    FREE
}