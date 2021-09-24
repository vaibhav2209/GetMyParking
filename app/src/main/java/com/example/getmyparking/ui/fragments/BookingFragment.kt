package com.example.getmyparking.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.getmyparking.R
import com.example.getmyparking.data.local.ParkingEntity
import com.example.getmyparking.databinding.FragmentBookingBinding
import com.example.getmyparking.models.ValueAddedServices
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
            toastMessage("Successfully Booked")
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imgInfo.setOnClickListener {
            navigateToNextScreen(R.id.action_bookingFragment_to_parkingDetailFragment)
        }

    }

    private fun setupObserver(){
        parkingViewModel.selectedParkingDetail.observe(viewLifecycleOwner){
            it?.let {
                setupInfo(it)
                bookingViewModel.setParkingLots(it.parkingLots)
            }
        }
        bookingViewModel.totalPrice.observe(viewLifecycleOwner){
            it?.let {
                binding.txtTotalPrice.text = getString(R.string.total_price_value, it.toString())
            }
        }
        binding.checkboxMaintenance.setOnCheckedChangeListener { buttonView, isChecked ->
            bookingViewModel.updateVASList(
                ValueAddedServices(
                    serviceName = "Regular Maintenance",
                    price = 1000.0
                )
            )

        }
        binding.checkboxCleanup.setOnCheckedChangeListener { buttonView, isChecked ->
            bookingViewModel.updateVASList(
                ValueAddedServices(
                    serviceName = "Car Sanitization and Cleanup",
                    price = 500.0
                )
            )

        }
        binding.checkboxWash.setOnCheckedChangeListener { buttonView, isChecked ->
            bookingViewModel.updateVASList(
                ValueAddedServices(
                    serviceName = "Car Wash",
                    price = 50.0
                )
            )

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

        (binding.textInputVehicleType.editText as? AutoCompleteTextView)?.setAdapter(vehicleTypeAdapter)
        binding.autoCompleteVehicleType.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                bookingViewModel.setSelectedVehicleType(vehicleList[position])
            }
    }

    private fun setupHoursDropDown(){
        val hoursAdapter = ArrayAdapter(requireContext(), R.layout.list_item, hoursList)
        (binding.textInputHours.editText as? AutoCompleteTextView)?.setAdapter(hoursAdapter)
        binding.autoCompleteHours.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                bookingViewModel.setHourlyPrice(hoursList[position])
            }
    }





}