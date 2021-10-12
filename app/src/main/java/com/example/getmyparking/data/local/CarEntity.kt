package com.example.getmyparking.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.getmyparking.utils.enums.ParkingLotType
import com.google.android.gms.maps.GoogleMap

@Entity
data class CarEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val model:String,
    val number:String,
    val type: ParkingLotType
){

}