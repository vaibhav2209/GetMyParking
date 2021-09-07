package com.example.getmyparking.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.example.getmyparking.R
import com.example.getmyparking.databinding.FragmentBookingBinding
import com.example.getmyparking.others.enums.VehicleType
import com.example.getmyparking.viewModel.BaseViewModel
import com.example.getmyparking.viewModel.BookingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingFragment : BaseFragment<FragmentBookingBinding>() {


    private var selectedVehicleType:VehicleType = VehicleType.CAR
    private val hoursList = arrayListOf(1, 2, 4, 8, 12, 24)
    private var selectedHour:Int = hoursList[0]
    private val bookingViewModel:BookingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentBookingBinding.inflate(inflater, container, false)
        setupUI()
        return binding.root
    }

    private fun setupUI(){
        binding.btnProceed.setOnClickListener {

        }

        val vehicleTypeAdapter = ArrayAdapter(requireContext(), R.layout.list_item, VehicleType.values())
        val hoursAdapter = ArrayAdapter(requireContext(), R.layout.list_item, hoursList)
        binding.autoCompleteVehicleType.setAdapter(vehicleTypeAdapter)
        binding.autoCompleteHours.setAdapter(hoursAdapter)
        binding.autoCompleteVehicleType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedVehicleType = VehicleType.values()[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        binding.autoCompleteHours.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedHour = hoursList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

    }





}