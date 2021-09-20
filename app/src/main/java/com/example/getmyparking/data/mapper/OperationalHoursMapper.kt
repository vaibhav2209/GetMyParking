package com.example.getmyparking.data.mapper

import com.example.getmyparking.data.local.OperationalHoursEntity
import com.example.getmyparking.models.OperationalHours

fun OperationalHours.toOperationalHoursEntity() = OperationalHoursEntity(
    id = id,
    lastUpdateBy = lastUpdateBy,
    openTime = openTime,
    closeTime = closeTime,
    day = day,
    parkingId = parkingId
)