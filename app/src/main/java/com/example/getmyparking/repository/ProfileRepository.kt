package com.example.getmyparking.repository

import com.example.getmyparking.data.local.CarEntity
import javax.inject.Inject

class ProfileRepository @Inject constructor (

):BaseRepository() {

    suspend fun addVehicleToDB(carEntity: CarEntity){
        parkingDao.insertVehicle(carEntity = carEntity)
    }

    fun getAllVehicles() = parkingDao.getAllVehicles()

    suspend fun deleteVehicle(carEntity: CarEntity) = parkingDao.deleteVehicle(carEntity)

    suspend fun editVehicle(carEntity: CarEntity) = parkingDao.editVehicle(
        id = carEntity.id,
        model = carEntity.model,
        number = carEntity.number,
        type = carEntity.type
    )

    suspend fun getVehicleById(id:Int) = parkingDao.getVehicleById(id)
}