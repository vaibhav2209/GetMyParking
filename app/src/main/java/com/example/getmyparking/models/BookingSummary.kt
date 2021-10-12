package com.example.getmyparking.models

data class BookingSummary(
    val startTime:String,
    val endTime:String,
    val totalPrice: Double,
    val totalTime:Int,
    val carNo: String = "DL 10 AB 1667",
    val services: List<ValueAddedServices>
)
