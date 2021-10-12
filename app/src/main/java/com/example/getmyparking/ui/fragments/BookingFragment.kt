package com.example.getmyparking.ui.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.getmyparking.R
import com.example.getmyparking.data.local.CarEntity
import com.example.getmyparking.data.local.ParkingEntity
import com.example.getmyparking.databinding.FragmentBookingBinding
import com.example.getmyparking.models.ValueAddedServices
import com.example.getmyparking.utils.Utilities
import com.example.getmyparking.viewModel.*
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber.d

@AndroidEntryPoint
class BookingFragment : BaseFragment<FragmentBookingBinding>() {

    private val hoursList = arrayListOf(1, 2, 4, 8, 12, 24)
    private val bookingViewModel: BookingViewModel by viewModels()
    private val parkingViewModel:ParkingViewModel by activityViewModels()
    private val bookingSummaryViewModel:BookingSummaryViewModel by activityViewModels()
    private val profileViewModel:ProfileViewModel by activityViewModels()
    private lateinit var hoursAdapter:ArrayAdapter<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentBookingBinding.inflate(inflater, container, false)
        setupUI()
        setupObserver()
        setupHoursDropDown()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        hoursAdapter = ArrayAdapter(requireContext(), R.layout.list_item, hoursList)
        (binding.textInputHours.editText as? AutoCompleteTextView)?.setAdapter(hoursAdapter)

    }


    private fun setupUI(){
        binding.btnProceed.setOnClickListener {
            validateBookingInformation()
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imgInfo.setOnClickListener {
            navigateToNextScreen(R.id.action_bookingFragment_to_parkingDetailFragment)
        }

        binding.checkboxMaintenance.setOnCheckedChangeListener { buttonView, isChecked ->
            bookingViewModel.updateVAS(
                ValueAddedServices(
                    serviceName = "Regular Maintenance",
                    price = 1000.0
                ),
                isChecked
            )

        }
        binding.checkboxCleanup.setOnCheckedChangeListener { buttonView, isChecked ->
            bookingViewModel.updateVAS(
                ValueAddedServices(
                    serviceName = "Car Sanitization and Cleanup",
                    price = 500.0
                ),
                isChecked
            )

        }
        binding.checkboxWash.setOnCheckedChangeListener { buttonView, isChecked ->
            bookingViewModel.updateVAS(
                ValueAddedServices(
                    serviceName = "Car Wash",
                    price = 50.0
                ),
                isChecked
            )

        }

        binding.editStartTime.setOnClickListener {
            openTimePicker()
        }

    }

    private fun validateBookingInformation() {
        when{
            binding.autoCompleteVehicleType.text?.isEmpty() == true ->{
                binding.autoCompleteVehicleType.error = "Select Vehicle"
            }

            binding.editStartTime.text?.isEmpty() == true ->{
                binding.editStartTime.error = "Select start time"
            }
            binding.autoCompleteHours.text?.isEmpty() == true ->{
                binding.autoCompleteHours.error = "Select Hours"
            }
            else ->{
                val summary = bookingViewModel.generateParkingSummary()
                bookingSummaryViewModel.setBookingSummary(summary)
                navigateToNextScreen(R.id.action_bookingFragment_to_bookingSummaryFragment)
            }
        }
    }

    private fun setupObserver(){
        parkingViewModel.selectedParkingDetail.observe(viewLifecycleOwner){
            it?.let {
                setupInfo(it)
                bookingViewModel.setParkingLots(it.parkingLots)
            }
        }
        profileViewModel.vehicles.observe(viewLifecycleOwner){
            it?.let { list->
                setupVehicle(list)
            }
        }

        bookingViewModel.startTime.observe(viewLifecycleOwner){
            it?.let {
                binding.editStartTime.text = it.second
                binding.txtHoursHeading.visibility = View.VISIBLE
                binding.textInputHours.visibility = View.VISIBLE
            }
        }

        bookingViewModel.totalPrice.observe(viewLifecycleOwner){
            it?.let {
                binding.txtTotalPrice.text = getString(R.string.total_price_value, it.toString())
            }
        }
        bookingViewModel.selectedHours.observe(viewLifecycleOwner){
            it?.let {
                binding.autoCompleteHours.setText(it.hour.toString(), false)
            }
        }

        bookingViewModel.selectedVehicleType.observe(viewLifecycleOwner){
            it?.let {
                binding.autoCompleteVehicleType.setText(it.carEntity.model, false)
            }
        }
    }

    private fun setupInfo(parkingEntity: ParkingEntity){
        parkingEntity.apply {
            binding.txtOpeningHoursTime.text = operationalHours?.first()?.openTime
            binding.txtParkingName.text = displayName
            binding.txtCityName.text = city
        }
    }

    private fun setupVehicle(list: List<CarEntity>){
        val vehicleTypeAdapter = ArrayAdapter(requireContext(), R.layout.list_item, list.map { it.model })
        (binding.textInputVehicleType.editText as? AutoCompleteTextView)?.setAdapter(vehicleTypeAdapter)
        binding.autoCompleteVehicleType.setOnClickListener {
            if (list.isEmpty()){
                d("vehicle list empty")
                showAlertDialog(
                    positiveText = "Add",
                    negativeBtnText = "cancel",
                    title = "You do not have any vehicles!",
                    message = "Do you want to add new vehicle?",
                    isCancelable = false,
                    positiveBtnClick = {
                        navigateToNextScreen(R.id.action_bookingFragment_to_profileFragment)
                    }
                )
            }else{
                binding.autoCompleteVehicleType.onItemClickListener =
                    AdapterView.OnItemClickListener { parent, view, position, id ->
                        bookingViewModel.setSelectedVehicleType(list[position])
                    }
            }
        }
    }

    private fun setupHoursDropDown(){
        binding.autoCompleteHours.freezesText = false
        binding.autoCompleteHours.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                bookingViewModel.setHourPrice(hoursList[position])
            }
    }

    private val timePickerListener: TimePickerDialog.OnTimeSetListener =
        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            d("TimePicker: hoursOfDay: $hourOfDay,  minute: $minute")
            bookingViewModel.setStartTime(hourOfDay, minute)
        }

    private fun openTimePicker(){
        val pair = Utilities.getCurrentTimeHoursMinute()
        val timePicker = TimePickerDialog(
            requireContext(),
            timePickerListener,
            pair.first,
            pair.second,
            false
        )
        timePicker.show()
    }



}