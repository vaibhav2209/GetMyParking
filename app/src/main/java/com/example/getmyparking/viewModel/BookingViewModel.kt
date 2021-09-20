package com.example.getmyparking.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.getmyparking.data.local.ParkingLotEntity
import com.example.getmyparking.models.ValueAddedServices
import com.example.getmyparking.utils.enums.ParkingLotType
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
) : BaseViewModel() {

    private val _vasList = MutableLiveData<List<ValueAddedServices>>()

    private val _currentHours = MutableLiveData<Double>()

    private val _selectedVehicleType = MutableLiveData<ParkingLotEntity>()
    val selectedVehicleType: LiveData<ParkingLotEntity> = _selectedVehicleType

    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double> = _totalPrice

    private val _parkingLots = MutableLiveData<List<ParkingLotEntity>>()
    val parkingLots: LiveData<List<ParkingLotEntity>> = _parkingLots

    fun setSelectedVehicleType(parkingLotType: ParkingLotType?) {
        parkingLotType?.let { type ->
            _parkingLots.value?.let { parkingLotEntity ->
                val parkingLotDetail = parkingLotEntity.find { it.type == parkingLotType }
                _selectedVehicleType.value = parkingLotDetail
            }
        }
    }

    fun setParkingLots(parkingLotEntities: List<ParkingLotEntity>) {
        _parkingLots.value = parkingLotEntities
    }

    private fun updateTotalPrice() {
        Timber.d("updateTotalPRice")
        /*_totalPrice.value = _vasList.value?.map { it.price }?.sum()?.let {
            _currentHours.value?.plus(
                it
            )
        }*/
        _totalPrice.value = _currentHours.value
    }

    fun setHourlyPrice(hour: Int) {
        Timber.d("setHourly $hour")
        _selectedVehicleType.value?.let { parkingLot ->
            val hourCost: Double? = when (hour) {
                1 -> parkingLot.cost1Hour?.toDouble()
                2 -> parkingLot.cost2Hour?.toDouble()
                4 -> parkingLot.cost4Hour?.toDouble()
                8 -> parkingLot.cost8Hour?.toDouble()
                12 -> parkingLot.cost12Hour?.toDouble()
                24 -> parkingLot.cost24Hour?.toDouble()
                else -> 0.0
            }
            Timber.d("setHourly: hourCost $hourCost")
            _currentHours.value = hourCost
            updateTotalPrice()
        }
    }


}