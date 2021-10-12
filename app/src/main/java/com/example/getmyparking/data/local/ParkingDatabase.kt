package com.example.getmyparking.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.getmyparking.models.ContractorInfo
import com.example.getmyparking.models.OperationalHours
import com.example.getmyparking.models.Parking
import com.example.getmyparking.models.ParkingLot

@Database(
    entities = [ParkingEntity::class, ContractorInfoEntity::class, CityEntity::class, CarEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ParkingDatabase: RoomDatabase() {
    abstract fun getParkingDao(): ParkingDao
}