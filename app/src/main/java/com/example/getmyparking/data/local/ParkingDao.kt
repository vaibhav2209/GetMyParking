package com.example.getmyparking.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.coroutines.flow.Flow


@Dao
interface ParkingDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertParkingLocations(parkingList: List<ParkingEntity>)

    @Query("SELECT * FROM ParkingEntity WHERE city LIKE :city")
     fun getParkingOfCity(city: String): Flow<List<ParkingEntity>>

     @Query("SELECT * FROM ParkingEntity WHERE (latitude BETWEEN :swLat AND :neLat) AND (longitude BETWEEN :swLong AND :neLong)")
     fun getParkingBetweenBounds(swLat:Double,swLong:Double,neLat:Double,neLong:Double) :Flow<List<ParkingEntity>>

     @Query("SELECT EXISTS (SELECT city FROM ParkingEntity WHERE city LIKE :city)")
     fun isCityParkingAvailable(city: String):Flow<Boolean>
}