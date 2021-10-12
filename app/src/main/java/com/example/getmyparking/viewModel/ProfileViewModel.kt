package com.example.getmyparking.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.getmyparking.data.local.CarEntity
import com.example.getmyparking.models.Vehicle
import com.example.getmyparking.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.net.IDN
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
 private val profileRepository: ProfileRepository
) : BaseViewModel(){


    private val _vehicles = MutableLiveData<List<CarEntity>>()
    val vehicles:LiveData<List<CarEntity>> = _vehicles

    private val _isEditing = MutableLiveData<Pair<Boolean, Int?>>()
    val isEditing:LiveData<Pair<Boolean, Int?>> = _isEditing

    private val _editVehicle = MutableLiveData<CarEntity>()
    val editVehicle:LiveData<CarEntity> = _editVehicle

    init {
        getVehicles()
    }

    fun addVehicle(carEntity: CarEntity) = viewModelScope.launch{
        profileRepository.addVehicleToDB(carEntity)
    }

    private fun getVehicles() = viewModelScope.launch {
        profileRepository.getAllVehicles().collect { vehicles->
            _vehicles.value = vehicles
        }
    }


    fun deleteVehicle(carEntity: CarEntity) = viewModelScope.launch {
        profileRepository.deleteVehicle(carEntity)
    }


    fun setEditVehicle(isEdit:Boolean, id:Int? = null) = viewModelScope.launch {
        _isEditing.value = Pair(isEdit, id)
    }

    fun editVehicle(carEntity: CarEntity) = viewModelScope.launch {
        _editVehicle.value?.let {
            val updated = CarEntity(
                id = it.id,
                model = carEntity.model,
                type = carEntity.type,
                number = carEntity.number
            )
            _editVehicle.value = updated
            profileRepository.editVehicle(updated)
        }
    }

    fun getVehicleById(id: Int) = viewModelScope.launch {
        profileRepository.getVehicleById(id).collect {
            _editVehicle.value = it
        }
    }


}