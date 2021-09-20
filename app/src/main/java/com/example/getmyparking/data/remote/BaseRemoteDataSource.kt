package com.example.getmyparking.data.remote

import android.util.Log
import com.example.getmyparking.data.dto.ParkingErrorDto
import com.example.getmyparking.repository.HMACSHA256Generator
import com.example.getmyparking.utils.Resource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Response
import javax.inject.Inject
import kotlin.jvm.Throws

abstract class BaseRemoteDataSource {

    @Inject
    protected lateinit var hmacshA256Generator: HMACSHA256Generator

    @Throws(Exception::class)
    protected fun getAuthorizeHeader(key:String): String {
        return try {
            hmacshA256Generator.authorizeHeaderGenerator(key)
        }catch (e: Exception){
            ""
        }
    }


    protected fun getCurrentTime():String =
        hmacshA256Generator.getCurrentTime()

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Resource.Success(body)
            }
            return Resource.Error(response.errorBody()?.string() ?: "Unknown Error Occurred")
        } catch (e: Exception) {
            return Resource.Error(e.message ?: e.toString())
        }
    }
}