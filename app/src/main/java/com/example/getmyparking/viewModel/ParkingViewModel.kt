package com.example.getmyparking.viewModel

import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.getmyparking.data.local.ParkingEntity
import com.example.getmyparking.repository.ParkingRepository
import com.example.getmyparking.utils.Resource
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class ParkingViewModel @Inject constructor(
    private val parkingRepository: ParkingRepository
) : BaseViewModel(){

    private val _parkingList = MutableLiveData<Resource<List<ParkingEntity>>>()
    val parkingList:LiveData<Resource<List<ParkingEntity>>> = _parkingList

    private val _searchedLocations = MutableLiveData<Resource<List<Address>>>()
    val searchedLocations:LiveData<Resource<List<Address>>> = _searchedLocations


    private val _currentViewedParking = MutableLiveData<List<ParkingEntity>>()
    val currentViewedParking:LiveData<List<ParkingEntity>> = _currentViewedParking

    private val _markerList = MutableLiveData<List<Marker>>()

    private val _selectedParkingDetail = MutableLiveData<ParkingEntity>()
    val selectedParkingDetail : LiveData<ParkingEntity> = _selectedParkingDetail


    fun setSearchedLocation(addresses: Resource<List<Address>>?){
        addresses?.let {
            _searchedLocations.postValue(addresses)
        }
    }

    fun addMarkers(markerList: List<Marker>){
        _markerList.value = markerList
    }

    fun getSelectedMarkerDetail(position:LatLng) = viewModelScope.launch(Dispatchers.IO) {
        val parkingEntity =  _currentViewedParking.value?.find {
            it.latitude == position.latitude && it.longitude == position.longitude
        }
        _selectedParkingDetail.postValue(parkingEntity)
    }

    fun getParkingForCity(city:String, shouldFetch:Boolean = false) = viewModelScope.launch(Dispatchers.IO) {
        parkingRepository.getParking(city, shouldFetch).collect {resource->
            when(resource){
                is Resource.Success -> {
                    resource.data?.let {
                        Timber.tag("ApiCheck").d(" viewModel: Success: ${it.size}")
                        _parkingList.postValue(Resource.Success(it))
                    }
                }
                is Resource.Loading -> {
                    Timber.tag("ApiCheck").d(" viewModel: loading:")
                    _parkingList.postValue(Resource.Loading())
                }
                is Resource.Error -> {
                    Timber.tag("ApiCheck").d(" viewModel: error: ${resource.message}")
                    resource.message?.let {
                        _parkingList.postValue(Resource.Error(it))
                    }
                }
            }
        }
    }

    fun getParkingForCurrentBound(bound: LatLngBounds) = viewModelScope.launch(Dispatchers.IO){
        val temp = ArrayList<ParkingEntity>()
        _parkingList.value?.data?.forEach { parkingEntity ->
            if(bound.contains(LatLng(parkingEntity.latitude, parkingEntity.longitude))){
                temp.add(parkingEntity)
            }
        }
        _currentViewedParking.postValue(temp)
    }

    fun getParkingBetweenBound(bound: LatLngBounds) = viewModelScope.launch(Dispatchers.IO) {
        parkingRepository.getParkingBetweenBounds(bound).collect { parking->
            Timber.tag("BoundCheck").d("${parking.map { it.city }}")
            _currentViewedParking.postValue(parking)
        }
    }




}