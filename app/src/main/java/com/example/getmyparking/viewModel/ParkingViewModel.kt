package com.example.getmyparking.viewModel

import android.location.Address
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.getmyparking.models.Parking
import com.example.getmyparking.models.ParkingList
import com.example.getmyparking.models.ParkingLot
import com.example.getmyparking.network.GsonRequest
import com.example.getmyparking.network.RequestType
import com.example.getmyparking.network.WebServiceProvider
import com.example.getmyparking.repository.ParkingRepository
import com.example.getmyparking.utils.Resource
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ParkingViewModel @Inject constructor(
    private val parkingRepository: ParkingRepository
) : BaseViewModel(){

    private val _parkingList = MutableLiveData<Resource<ParkingList>>()
    val parkingList:LiveData<Resource<ParkingList>> = _parkingList

    private val _searchedLocations = MutableLiveData<Resource<List<Address>>>()
    val searchedLocations:LiveData<Resource<List<Address>>> = _searchedLocations

    fun setSearchedLocation(addresses: Resource<List<Address>>?){
        addresses?.let {
            _searchedLocations.postValue(addresses)
        }
    }


    fun getParkingOfCity(pageSize:Int, pageNo: Int , city:String) = viewModelScope.launch {
        _parkingList.postValue(Resource.Loading())
        try{
            val response = parkingRepository.getParkingOfCity(
                pageSize = pageSize,
                pageNo = pageNo,
                city = city
            )
            if (response.isSuccessful) {
                response.body()?.let { parkingList ->
                    _parkingList.postValue(Resource.Success(parkingList))
                }
            } else {
                _parkingList.postValue(response.errorBody()?.let { Resource.Error(it.string()) })
            }
        }catch (e: Exception){
            _parkingList.postValue(e.message?.let { Resource.Error(it) })
        }
    }
}