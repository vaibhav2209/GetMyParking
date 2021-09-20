package com.example.getmyparking.data.remote

import com.example.getmyparking.models.ParkingList
import retrofit2.Response
import retrofit2.http.*

interface ParkingApi {

    @Headers( "x_gmp_tenant: gmp")
    @GET("parkingdata/v1/parking/{username}")
    suspend fun getParkingOfCity(
        @Path("username") username:String,
        @Query("pageSize") pageSize: Int,
        @Query("pageNo") pageNo: Int,
        @Query("cities") city: String,
        @Header("x-date") date:String,
        @Header("Authorization") authorization: String
    ): Response<ParkingList>

}