package com.example.getmyparking.data.local

import androidx.room.*
import com.example.getmyparking.utils.enums.ParkingLotType
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

     @Insert(onConflict = OnConflictStrategy.IGNORE)
     suspend fun insertCity(cityEntity: CityEntity)

     @Insert(onConflict = OnConflictStrategy.IGNORE)
     suspend fun insertVehicle(carEntity: CarEntity)

     @Query("SELECT * FROM CarEntity")
     fun getAllVehicles():Flow<List<CarEntity>>

     @Delete
     suspend fun deleteVehicle(carEntity: CarEntity)

     @Query("UPDATE CarEntity SET type = :type , model = :model , number = :number WHERE id =:id")
     suspend fun editVehicle(id: Int, model:String ,number: String, type: ParkingLotType)

     @Query("SELECT * FROM CarEntity WHERE id LIKE :id")
     fun getVehicleById(id: Int):Flow<CarEntity>

}