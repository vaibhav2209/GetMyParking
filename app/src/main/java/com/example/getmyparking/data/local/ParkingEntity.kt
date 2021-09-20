package com.example.getmyparking.data.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.getmyparking.models.ContractorInfo
import com.example.getmyparking.models.OperationalHours
import com.example.getmyparking.utils.enums.*


@Entity
data class ParkingEntity(
    @PrimaryKey
    @ColumnInfo(name = "ParkingId")
    val id:Int,
    val city:String,
    val name:String?,
    val displayName:String?,
    val address:String?,
    val landmark:String?,
    val category: ParkingCategory?,
    val type: ParkingType?,
    val ownership: ParkingOwnership?,
    val gateType: GateType?,
    val latitude: Double,
    val longitude:Double,
    val feeType: ParkingFeeType?,
    val entrancePhoto:String?,
    val entrancePhotoAdditional:String?,
    val structure: ParkingStructure?,
    val heightRestriction:String?,
    val noFloors:Int?,
    val surfaceType:String?,
    val cctvInstalled:Int?,
    val powerSupplyAvailable:Int?,
    val ticketingSystem: TicketingSystem?,
    val ticketingSystemCompany:String?,
    val sameOperationalHours:Int?,
    val operationalHourNotes:String?,
    val countryCode:Int?,
    val region:String?,
    val zipCode:Int?,
    val webUrl:String?,
    val description:String?,
    val areaPhoto:String?,
    val areaPhoto2:String?,
    val receiptPhoto:String?,
    val weekdayAverage:Int?,
    val weekendAverage:Int?,
    val extraPhoto:String?,
    val lastUpdateBy:String?,
    val isParkingEnabled:Int?,
    val externalId:Int?,
    val cpmsKey:String?,
    val operationalHours: List<OperationalHoursEntity>?,
    @Embedded(prefix = "contractorInfo")
    val contractorInfo: ContractorInfoEntity?,
    val parkingLots : List<ParkingLotEntity>?,
    val paymentMethods : List<PaymentMethodType>,
    val isDynamicParking: Boolean
)