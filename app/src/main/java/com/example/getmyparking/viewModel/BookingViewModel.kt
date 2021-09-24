package com.example.getmyparking.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.getmyparking.data.local.ParkingLotEntity
import com.example.getmyparking.models.ValueAddedServices
import com.example.getmyparking.utils.enums.ParkingLotType
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.internal.toImmutableList
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
) : BaseViewModel() {

    private val _vasList = ArrayList<ValueAddedServices>()

    private val _currentHours = MutableLiveData<Double>()
    private val _vasTotalPrice = MutableLiveData<Double>()

    private var _selectedVehicleType:ParkingLotEntity? = null

    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double> = _totalPrice

    private val _parkingLots = MutableLiveData<List<ParkingLotEntity>>()
    val parkingLots: LiveData<List<ParkingLotEntity>> = _parkingLots

    fun setSelectedVehicleType(parkingLotType: ParkingLotType?) {
        parkingLotType?.let { type ->
            _parkingLots.value?.let { parkingLotEntity ->
                val parkingLotDetail = parkingLotEntity.find { it.type == type }
                _selectedVehicleType = parkingLotDetail
            }
        }
    }

    fun setParkingLots(parkingLots: List<ParkingLotEntity>?) = parkingLots?.let { _parkingLots.value = it }

    fun updateVASList(vas:ValueAddedServices){
        Timber.tag("BookingFrag").d("updateVASlist")
        _vasList.apply {
            if(this.contains(vas)){
                this.remove(vas)
            }else{
                this.add(vas)
            }
            Timber.tag("BookingFrag").d("updateVASlist $this")
            setVASTotalPrice()
        }
    }

    private fun updateTotalPrice() {
        var vasPrice = 0.0
        var hourPrice = 0.0
        _vasTotalPrice.value?.let {
            vasPrice = it
        }
        _currentHours.value?.let {
            hourPrice = it
        }
        Timber.tag("BookingFrag").d("updateTotalPRice vas: $vasPrice , hour: $hourPrice")
        _totalPrice.value = vasPrice + hourPrice
    }

    private fun setVASTotalPrice(){
        _vasList.let { list->
            val total = list.map { it.price }.sum()
            _vasTotalPrice.value = total
        }
        Timber.tag("BookingFrag").d("setVASTotal")
        updateTotalPrice()
    }

    fun setHourlyPrice(hour: Int) {
        Timber.tag("BookingFrag").d("setHourly $hour")
        _selectedVehicleType?.let { parkingLot ->
            val hourCost: Double? = when (hour) {
                1 -> parkingLot.cost1Hour?.toDouble()
                2 -> parkingLot.cost2Hour?.toDouble()
                4 -> parkingLot.cost4Hour?.toDouble()
                8 -> parkingLot.cost8Hour?.toDouble()
                12 -> parkingLot.cost12Hour?.toDouble()
                24 -> parkingLot.cost24Hour?.toDouble()
                else -> 0.0
            }
            Timber.tag("BookingFrag").d("setHourly: hourCost $hourCost")
            _currentHours.value = hourCost
            updateTotalPrice()
        }
    }


}