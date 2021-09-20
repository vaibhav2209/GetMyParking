package com.example.getmyparking.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.getmyparking.data.local.ParkingEntity
import com.example.getmyparking.utils.enums.ParkingLotType
import com.example.getmyparking.utils.enums.ParkingType

open class BaseViewModel :ViewModel(){

    private val _loadingState = MutableLiveData(false)
    val loadingState:LiveData<Boolean> = _loadingState

    fun setLoadingState(isLoading:Boolean){
        _loadingState.value = isLoading
    }
}