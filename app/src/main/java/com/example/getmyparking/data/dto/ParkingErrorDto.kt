package com.example.getmyparking.data.dto

import com.example.getmyparking.utils.enums.ParkingErrorCode

data class ParkingErrorDto (
 val transactionId:String,
 val message:String,
 val statusCode:Int,
 val parkingErrorCode:ParkingErrorCode
 )