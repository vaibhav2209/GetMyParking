package com.example.getmyparking.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContractorInfoEntity(
    @PrimaryKey
    @ColumnInfo(name = "ContractorInfoId")
    val id:Int,
    val parkingId:Int,
    val startDate: String?,
    val endDate: String?,
    val company: String?,
    val supervisorName: String?,
    val supervisorMobileNumber: String?,
    val contactPerson: String?,
    val mobileNumber: Int?,
    @ColumnInfo(name = "ContractorInfoLastUpdatedBy")
    val lastUpdateBy: String?
)