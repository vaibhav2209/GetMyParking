package com.example.getmyparking.data.mapper

import com.example.getmyparking.data.local.ParkingLotEntity
import com.example.getmyparking.data.local.ParkingPassEntity
import com.example.getmyparking.models.ParkingLot
import com.example.getmyparking.models.ParkingPass

fun ParkingLot.toParkingLotEntity() = ParkingLotEntity(
    capacity = capacity,
    cost12Hour = cost12Hour,
    cost1Hour = cost1Hour,
    cost24Hour = cost24Hour,
    cost2Hour = cost2Hour,
    cost4Hour = cost4Hour,
    cost8Hour = cost8Hour,
    fixedCharges = fixedCharges,
    id = id,
    lastUpdateBy = lastUpdateBy,
    parkingId = parkingId,
    parkingPassEnabled = parkingPassEnabled,
    parkingPasses = parkingPasses.map { it.toParkingPassEntity() },
    paymentAt = paymentAt,
    priceDescription = priceDescription,
    type = type
)

fun ParkingPass.toParkingPassEntity() = ParkingPassEntity(
    id = id,
    type = type,
    accessTech = accessTech,
    price = price,
    parkingLotId = parkingLotId,
    lastUpdateBy = lastUpdateBy
)