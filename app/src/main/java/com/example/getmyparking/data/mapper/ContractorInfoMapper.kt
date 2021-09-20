package com.example.getmyparking.data.mapper

import com.example.getmyparking.data.local.ContractorInfoEntity
import com.example.getmyparking.models.ContractorInfo

fun ContractorInfo.toContractorInfoEntity() = ContractorInfoEntity(
    id = id,
    lastUpdateBy = lastUpdateBy,
    mobileNumber = mobileNumber,
    parkingId = parkingId,
    startDate = startDate,
    endDate = endDate,
    company = company,
    supervisorMobileNumber = supervisorMobileNumber,
    supervisorName = supervisorName,
    contactPerson = contactPerson
)