package com.example.getmyparking.data.local

import androidx.room.TypeConverter
import com.example.getmyparking.utils.enums.PaymentMethodType
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun stringToOperationalHours(value: String) =
        Gson().fromJson(value, Array<OperationalHoursEntity>::class.java).toList()

    @TypeConverter
    fun operationalHoursToString(hoursList: List<OperationalHoursEntity>): String? =
        Gson().toJson(hoursList)

    @TypeConverter
    fun stringToParkingLot(value: String) =
        Gson().fromJson(value, Array<ParkingLotEntity>::class.java).toList()

    @TypeConverter
    fun parkingLotToString(hoursList: List<ParkingLotEntity>): String? =
        Gson().toJson(hoursList)

    @TypeConverter
    fun stringToParkingPass(value: String) =
        Gson().fromJson(value, Array<ParkingPassEntity>::class.java).toList()

    @TypeConverter
    fun parkingPassToString(hoursList: List<ParkingPassEntity>): String? =
        Gson().toJson(hoursList)

    @TypeConverter
    fun stringToParkingMethod(value: String) =
        Gson().fromJson(value, Array<PaymentMethodType>::class.java).toList()

    @TypeConverter
    fun parkingMethodToString(hoursList: List<PaymentMethodType>): String? =
        Gson().toJson(hoursList)

}