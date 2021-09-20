package com.example.getmyparking.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.getmyparking.models.ParkingPass
import com.example.getmyparking.utils.enums.ParkingLotType
import com.example.getmyparking.utils.enums.PaymentAt


data class ParkingLotEntity(
    val capacity: Int,
    val cost12Hour: Int?,
    val cost1Hour: Int?,
    val cost24Hour: Int?,
    val cost2Hour: Int?,
    val cost4Hour: Int?,
    val cost8Hour: Int?,
    val fixedCharges: Int?,
//    @ColumnInfo(name = "parkingLotId")
//    @PrimaryKey
    val id: Int,
    val lastUpdateBy: String?,
    val parkingId: Int,
    val parkingPassEnabled: Int?,
    val parkingPasses: List<ParkingPassEntity>?,
    val paymentAt: PaymentAt?,
    val priceDescription: String?,
    val type: ParkingLotType?
)