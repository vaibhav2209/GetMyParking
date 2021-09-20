package com.example.getmyparking.data.remote

import com.example.getmyparking.utils.Constants
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class ParkingRemoteDataSource @Inject constructor(
    private val parkingApi: ParkingApi
):BaseRemoteDataSource() {

    suspend fun getParkingOfCity(pageSize: Int = 10, pageNo: Int , city: String) = getResult {
        val authorization = getAuthorizeHeader(Constants.SECRET_KEY)
        Timber.tag("ApiCheck").d(" authorization: $authorization")
        val date = getCurrentTime()
        Timber.tag("ApiCheck").d(" date: $date")
        parkingApi.getParkingOfCity(
            pageSize = pageSize,
            pageNo = pageNo,
            city = city,
            authorization = authorization,
            date = date,
            username = Constants.USER_NAME
        )
    }
}