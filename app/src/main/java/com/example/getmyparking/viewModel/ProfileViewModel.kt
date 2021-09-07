package com.example.getmyparking.viewModel

import androidx.lifecycle.MutableLiveData
import com.example.getmyparking.models.Parking
import com.example.getmyparking.models.Vehicle
import com.example.getmyparking.network.WebServiceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(

) : BaseViewModel(){

    private val vehicleList = ArrayList<Vehicle>()
    val vehicleData = MutableLiveData<List<Vehicle>>()

    fun addVehicle(vehicle: Vehicle){
        vehicleList.add(vehicle)
        vehicleData.postValue(vehicleList)
    }




}