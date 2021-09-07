package com.example.getmyparking.utils.enums

import com.google.gson.annotations.SerializedName

enum class GateType {
    @SerializedName("AUTOMATIC_BOOM_BARRIER")
    AUTOMATIC_BOOM_BARRIER,
    @SerializedName("MANUAL_BOOM_BARRIER")
    MANUAL_BOOM_BARRIER,
    @SerializedName("NORMAL_METAL_GATE")
    NORMAL_METAL_GATE,
    @SerializedName("NO_GATE")
    NO_GATE
}