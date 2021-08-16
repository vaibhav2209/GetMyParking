package com.example.getmyparking.services

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.getmyparking.utils.Constants.FASTEST_LOCATION_INTERVAL
import com.example.getmyparking.utils.Constants.LOCATION_UPDATE_INTERVAL
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocationServices: LifecycleService(){

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    override fun onCreate() {
        super.onCreate()
    }

    companion object{
        val updatedLocations = MutableLiveData<List<Location>>()
    }

    @SuppressLint("MissingPermission")
    private fun updateLocation(){
        val request = LocationRequest.create().apply {
            interval = LOCATION_UPDATE_INTERVAL
            fastestInterval = FASTEST_LOCATION_INTERVAL
            priority = PRIORITY_HIGH_ACCURACY
        }
        fusedLocationProviderClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun removeLocationUpdate() =
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            updatedLocations.postValue(result.locations)
        }
    }
}