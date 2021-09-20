package com.example.getmyparking.data.mapper

import com.example.getmyparking.data.local.ParkingEntity
import com.example.getmyparking.models.Parking
import com.example.getmyparking.utils.enums.ParkingFeeType

fun Parking.toParkingEntity() = ParkingEntity(
    id = id,
    city = city,
    name = name,
    displayName = displayName,
    address = address,
    landmark = landmark,
    category = category,
    type = type,
    ownership = ownership,
    gateType = gateType,
    latitude = geoLocation.latitude,
    longitude = geoLocation.longitude,
    feeType = feeType,
    entrancePhoto = entrancePhoto,
    entrancePhotoAdditional = entrancePhotoAdditional,
    structure = structure,
    heightRestriction = heightRestriction,
    noFloors = noFloors,
    surfaceType = surfaceType,
    cctvInstalled = cctvInstalled,
    powerSupplyAvailable = powerSupplyAvailable,
    ticketingSystem = ticketingSystem,
    ticketingSystemCompany = ticketingSystemCompany,
    sameOperationalHours = sameOperationalHours,
    operationalHourNotes = operationalHourNotes,
    countryCode = countryCode,
    region = region,
    zipCode = zipCode,
    webUrl = webUrl,
    description = description,
    areaPhoto = areaPhoto,
    areaPhoto2 = areaPhoto2,
    receiptPhoto = receiptPhoto,
    weekdayAverage = weekdayAverage,
    weekendAverage = weekendAverage,
    extraPhoto = extraPhoto,
    lastUpdateBy = lastUpdateBy,
    isParkingEnabled = isParkingEnabled,
    externalId = externalId,
    cpmsKey = cpmsKey,
    operationalHours = operationalHours.map { it.toOperationalHoursEntity() },
    contractorInfo = contractorInfo.toContractorInfoEntity(),
    parkingLots = parkingLots.map { it.toParkingLotEntity() },
    paymentMethods = paymentMethods,
    isDynamicParking =  feeType != ParkingFeeType.FREE
)