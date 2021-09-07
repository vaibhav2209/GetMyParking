package com.example.getmyparking.models

data class ContractorInfo(
    val id:Int,
    val parkingId:Int,
    val startDate: String,
    val endDate: String,
    val company: String,
    val supervisorName: String,
    val supervisorMobileNumber: String,
    val contactPerson: String,
    val mobileNumber: Int,
    val lastUpdateBy: String
)
