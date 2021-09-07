package com.example.getmyparking.models

import com.example.getmyparking.utils.enums.ParkingLotType
import com.example.getmyparking.utils.enums.PaymentAt

data class ParkingLot(
    val capacity: Int,
    val cost12Hour: Int,
    val cost1Hour: Int,
    val cost24Hour: Int,
    val cost2Hour: Int,
    val cost4Hour: Int,
    val cost8Hour: Int,
    val fixedCharges: Int,
    val id: Int,
    val lastUpdateBy: String,
    val parkingId: Int,
    val parkingPassEnabled: Int,
    val parkingPasses: List<ParkingPass>,
    val paymentAt: PaymentAt,
    val priceDescription: String,
    val type: ParkingLotType
)