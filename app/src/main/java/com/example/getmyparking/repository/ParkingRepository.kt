package com.example.getmyparking.repository


import com.example.getmyparking.data.local.CityEntity
import com.example.getmyparking.data.local.ParkingEntity
import com.example.getmyparking.data.mapper.toParkingEntity

import com.example.getmyparking.data.remote.ParkingRemoteDataSource
import com.example.getmyparking.models.Parking
import com.example.getmyparking.models.ParkingList
import com.example.getmyparking.utils.Resource
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.coroutines.flow.*
import okhttp3.internal.toImmutableList
import timber.log.Timber
import javax.inject.Inject

class ParkingRepository @Inject constructor(
    private val parkingRemoteDataSource: ParkingRemoteDataSource,

):BaseRepository() {

    fun getParking(city: String, shouldFetch: Boolean = false) =
        flow<Resource<List<ParkingEntity>>> {
            emit(Resource.Loading())
            val data = getParkingFromLocal(city).firstOrNull()
                if (shouldFetch || data.isNullOrEmpty()) {
                    getAllParkingFromCity(city).collect { resource ->
                        when (resource) {
                            is Resource.Success -> {
                                resource.data?.let { parkingList ->
                                    insertParkingToDB(parkingList)
                                    //insertCityToDB(parkingList.first().city)
                                    getParkingFromLocal(city).collect {
                                        emit(Resource.Success(it))
                                    }
                                }
                            }
                            is Resource.Error -> {
                                resource.message?.let {
                                    emit(Resource.Error(it))
                                }
                            }
                            is Resource.Loading -> {
                                emit(Resource.Loading())
                            }
                        }
                    }
                } else {
                    emit(Resource.Success(data))
                }
        }

    private suspend fun insertParkingToDB(parkingList: List<ParkingEntity>) =
        parkingDao.insertParkingLocations(parkingList)

    private suspend fun insertCityToDB(city: String) =
        parkingDao.insertCity(CityEntity(city))


    private fun getParkingFromLocal(city: String) =
        parkingDao.getParkingOfCity(city)


    private suspend fun getAllParkingFromCity(city: String) = flow{
        var page = 1
        val tempParkingList = ArrayList<Parking>()
        while (page != -1){
            when(val response = getParkingFromRemote(page, city)){
                is Resource.Success -> {
                    response.data?.let {parkingList->
                        Timber.tag("ApiCheck").d(" repo: Success: ${parkingList.parkingList.map {it.city}}")
                        page += 1
                        if (parkingList.parkingList.isEmpty()){
                            page = -1
                        }
                        tempParkingList.addAll(parkingList.parkingList)
                    }
                }
                is Resource.Error -> {
                    Timber.tag("ApiCheck").d(" repo: error: ${response.message}")
                    emit(response.message?.let { Resource.Error(it) })
                    page = -1
                }
                is Resource.Loading -> {
                    emit(Resource.Loading())
                    Timber.tag("ApiCheck").d(" repo: loading: ")
                }
            }
        }
        emit(Resource.Success(tempParkingList.map { it.toParkingEntity() }))
    }


    private suspend fun getParkingFromRemote(pageNo: Int, city: String) =
       parkingRemoteDataSource.getParkingOfCity(pageNo = pageNo , city = city)


    fun getParkingBetweenBounds(latLngBounds: LatLngBounds) =
        parkingDao.getParkingBetweenBounds(
            neLat = latLngBounds.northeast.latitude,
            neLong = latLngBounds.northeast.longitude,
            swLat = latLngBounds.southwest.latitude,
            swLong = latLngBounds.southwest.longitude
        )
}