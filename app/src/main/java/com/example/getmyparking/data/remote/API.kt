package com.example.getmyparking.data.remote

object API {

    private const val baseUrl = "https://gateway.api.getmyparking.com"

    fun getParkingDataUrl(userName:String, city:String , pageSize:Int = 10, pageNo:Int = 1):String{
        return "$baseUrl/parkingdata/v1/parking/$userName?pageSize=$pageSize&pageNo=$pageNo&cities=$city"
    }

    const val BASE_URL = "https://gateway.api.getmyparking.com/"
}