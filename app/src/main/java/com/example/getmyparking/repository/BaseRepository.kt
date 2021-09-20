package com.example.getmyparking.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.getmyparking.data.local.ParkingDao
import com.example.getmyparking.utils.Resource
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.jvm.Throws


abstract class BaseRepository(
) {

    @Inject
    protected lateinit var hmacshA256Generator: HMACSHA256Generator

    @Inject
    protected lateinit var parkingDao: ParkingDao

    @Throws(Exception::class)
    protected fun getAuthorizeHeader(key:String): String =
        hmacshA256Generator.authorizeHeaderGenerator(key)

    protected fun getCurrentTime():String =
        hmacshA256Generator.getCurrentTime()



    /*protected fun <T, A> performGetOperation(databaseQuery: () -> LiveData<T>,
                                   networkCall: suspend () -> Resource<A>,
                                   saveCallResult: suspend (A) -> Unit): LiveData<Resource<T>> =
        liveData(Dispatchers.IO) {
            emit(Resource.Loading())
            val source = databaseQuery.invoke().map { Resource.Success(it) }
            emitSource(source)

            val responseStatus = networkCall.invoke()

        }*/
}