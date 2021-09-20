package com.example.getmyparking.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.example.getmyparking.R
import com.example.getmyparking.data.local.ParkingEntity
import com.example.getmyparking.databinding.FragmentBookingBinding
import com.example.getmyparking.utils.enums.ParkingLotType
import com.example.getmyparking.utils.enums.VehicleType
import com.example.getmyparking.viewModel.BookingViewModel
import com.example.getmyparking.viewModel.ParkingViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import timber.log.Timber.d
import timber.log.Timber.tag
import java.util.*

@AndroidEntryPoint
class BookingFragment : BaseFragment<FragmentBookingBinding>() {


    private var selectedVehicleType: VehicleType = VehicleType.CAR
    private val hoursList = arrayListOf(1, 2, 4, 8, 12, 24)
    private val bookingViewModel:BookingViewModel by activityViewModels()
    private val parkingViewModel:ParkingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentBookingBinding.inflate(inflater, container, false)
        setupUI()
        setupObserver()
        return binding.root
    }

    private fun setupUI(){
        setupHoursDropDown()
        binding.btnProceed.setOnClickListener {

        }

    }

    private fun setupObserver(){
        parkingViewModel.selectedParkingDetail.observe(viewLifecycleOwner){
            d("selectedParkingDetail: $it")
            it?.let { setupInfo(it) }
        }
        bookingViewModel.selectedVehicleType.observe(viewLifecycleOwner){
            it?.let { parkingLot->
                d("selectedVehicleType: $it")
            }
        }

        bookingViewModel.totalPrice.observe(viewLifecycleOwner){
            it?.let {
                binding.txtTotalPrice.text = getString(R.string.total_price_value, it.toString())
            }
        }
    }

    private fun setupInfo(parkingEntity: ParkingEntity){
        parkingEntity.apply {
            binding.txtOpeningHoursTime.text = operationalHours?.first()?.openTime
            binding.txtParkingName.text = displayName
            binding.txtCityName.text = city
            parkingLots?.map { it.type }?.let {setupVehicleDropDown(it) }
        }
    }

    private fun setupVehicleDropDown(vehicleList: List<ParkingLotType?>){
        val vehicleTypeAdapter = ArrayAdapter(requireContext(), R.layout.list_item, vehicleList.map { it?.name })

        binding.autoCompleteVehicleType.setAdapter(vehicleTypeAdapter)
        binding.autoCompleteVehicleType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                bookingViewModel.setSelectedVehicleType(vehicleList[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun setupHoursDropDown(){
        val hoursAdapter = ArrayAdapter(requireContext(), R.layout.list_item, hoursList)
        binding.autoCompleteHours.setAdapter(hoursAdapter)
        binding.autoCompleteHours.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                d("hour Item selected: $position")
                bookingViewModel.setHourlyPrice(hoursList[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }





}