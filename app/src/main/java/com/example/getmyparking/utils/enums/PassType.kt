package com.example.getmyparking.utils.enums

import com.google.gson.annotations.SerializedName

enum class PassType {
    @SerializedName("DAY")
    DAY,
    @SerializedName("WEEK")
    WEEK,
    @SerializedName("MONTH")
    MONTH
}