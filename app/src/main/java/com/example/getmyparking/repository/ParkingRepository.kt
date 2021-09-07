package com.example.getmyparking.repository

import com.example.getmyparking.models.ParkingList
import com.example.getmyparking.network.ParkingApi
import com.example.getmyparking.utils.Constants.SECRET_KEY
import com.example.getmyparking.utils.Constants.USER_NAME
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject
import kotlin.jvm.Throws

class ParkingRepository @Inject constructor(
    private val parkingApi: ParkingApi
):BaseRepository() {

    @Throws(Exception::class)
    suspend fun getParkingOfCity(pageSize:Int, pageNo:Int , city:String): Response<ParkingList> {
        try{
            val authorization = getAuthorizeHeader(SECRET_KEY)
            Timber.tag("ApiCheck").d(" authorization: $authorization")
            val date = getCurrentTime()
            Timber.tag("ApiCheck").d(" date: $date")
            return parkingApi.getParkingOfCity(
                pageSize = pageSize,
                pageNo = pageNo,
                city = city,
                authorization = authorization,
                date = date,
                username = USER_NAME
            )
        }catch (e: Exception){
            throw  e
        }
    }

}