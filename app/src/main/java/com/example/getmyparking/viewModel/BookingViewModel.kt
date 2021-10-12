package com.example.getmyparking.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.getmyparking.data.local.CarEntity
import com.example.getmyparking.data.local.ParkingLotEntity
import com.example.getmyparking.models.BookingSummary
import com.example.getmyparking.models.ValueAddedServices
import com.example.getmyparking.utils.Utilities
import com.example.getmyparking.utils.enums.ParkingLotType
import com.fasterxml.jackson.databind.AnnotationIntrospector.pair
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class BookingViewModel @Inject constructor(

):BaseViewModel() {

    private val _parkingLots = MutableLiveData<List<ParkingLotEntity>>()
    private var _selectedVehicleType = MutableLiveData<VehicleDetails>()
    val selectedVehicleType:LiveData<VehicleDetails> = _selectedVehicleType

    private val _vasList = MutableLiveData<ArrayList<ValueAddedServices>>(arrayListOf())
    private val _selectedHours = MutableLiveData<ParkingHours>()
    val selectedHours:LiveData<ParkingHours> = _selectedHours

    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice:LiveData<Double> = _totalPrice

    private val _startTime = MutableLiveData<Pair<Calendar, String>>()
    val startTime: LiveData<Pair<Calendar, String>> = _startTime

    private val _endTime = MutableLiveData<Pair<Calendar, String>>()

    fun setSelectedVehicleType(carEntity: CarEntity) {
        carEntity.let {car->
            _parkingLots.value?.let { parkingLotEntity ->
                val parkingLotDetail = parkingLotEntity.find { it.type == car.type }
                _selectedVehicleType.value = parkingLotDetail?.let {
                    VehicleDetails(
                        parkingLotEntity = it,
                        carEntity = carEntity
                    )
                }
            }
        }
    }

    fun setParkingLots(parkingLots: List<ParkingLotEntity>?) = parkingLots?.let { _parkingLots.value = it }

    fun updateVAS(vas:ValueAddedServices, isChecked:Boolean){
        _vasList.value?.let {
            if(!it.contains(vas) && isChecked){
                it.add(vas)
            }else if (!isChecked && it.contains(vas)){
                it.remove(vas)
            }
            _vasList.notifyObserver()
            updateTotalPrice()
        }
    }

    fun setHourPrice(hour: Int) {
        _selectedVehicleType.value?.parkingLotEntity?.let { parkingLot ->
            val hourCost: Double? = when (hour) {
                1 -> parkingLot.cost1Hour?.toDouble()
                2 -> parkingLot.cost2Hour?.toDouble()
                4 -> parkingLot.cost4Hour?.toDouble()
                8 -> parkingLot.cost8Hour?.toDouble()
                12 -> parkingLot.cost12Hour?.toDouble()
                24 -> parkingLot.cost24Hour?.toDouble()
                else -> 0.0
            }
            _selectedHours.value = hourCost?.let {
                ParkingHours(
                    hour = hour,
                    price = it
                )
            }
            updateTotalPrice()
            endTime(hour)
        }
    }

    private fun updateTotalPrice(){
        var hourPrice = 0.0
        var vasPrice = 0.0
        _selectedHours.value?.let {hours->
           hourPrice = hours.price
        }
        _vasList.value?.let { vasList->
            vasPrice = vasList.map { it.price }.sum()
        }
        _totalPrice.value = hourPrice + vasPrice
    }

    data class ParkingHours(
        val hour: Int,
        val price:Double
    )

    data class VehicleDetails(
        val parkingLotEntity: ParkingLotEntity,
        val carEntity: CarEntity
    )

    fun setStartTime(hourOfDay:Int, minute:Int){
        val startCalendar = Calendar.getInstance()
        startCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        startCalendar.set(Calendar.MINUTE, minute)
        val time = Utilities.convertTimeIntoString(startCalendar)
        _startTime.value = Pair(startCalendar, time)
    }

    private fun endTime(hour: Int) {
        _startTime.value?.let {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, it.first.get(Calendar.HOUR_OF_DAY))
            calendar.set(Calendar.MINUTE, it.first.get(Calendar.MINUTE))
            calendar.add(Calendar.HOUR_OF_DAY, hour)
            val time = Utilities.convertTimeIntoString(calendar)
            _endTime.value = Pair(calendar, time)
        }
    }

    fun generateParkingSummary(): BookingSummary {
        return BookingSummary(
            startTime =_startTime.value?.second ?: "N/A",
            endTime =_endTime.value?.second ?: "N/A",
            totalPrice = _totalPrice.value ?: 0.0,
            totalTime = _selectedHours.value?.hour ?: 0,
            services = _vasList.value ?: arrayListOf(),
            carNo = _selectedVehicleType.value?.carEntity?.number ?: "N/A"
        )
    }

}