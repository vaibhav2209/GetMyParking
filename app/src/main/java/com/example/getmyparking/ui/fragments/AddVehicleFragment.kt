package com.example.getmyparking.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.getmyparking.R
import com.example.getmyparking.databinding.FragmentAddVehicleBinding
import com.example.getmyparking.models.Vehicle
import com.example.getmyparking.utils.enums.VehicleType
import com.example.getmyparking.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddVehicleFragment : BaseFragment<FragmentAddVehicleBinding>() {

    private var selectedVehicleType: VehicleType = VehicleType.CAR
    private val profileViewModel:ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentAddVehicleBinding.inflate(inflater, container, false)
        setupUI()
        return binding.root
    }

    private fun setupUI(){
        binding.btnAddVehicleDone.setOnClickListener {
            retrieveVehicleData()
            findNavController().popBackStack()
        }
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, VehicleType.values())
        binding.autoCompleteVehicleType.setAdapter(adapter)
        binding.autoCompleteVehicleType.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
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

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun retrieveVehicleData() {
        if (binding.editTxtVehicleModel.text?.isEmpty() == true) {
            binding.textInputVehicleModel.error = "Enter vehicle model"
            return
        }
        if (binding.textInputVehicleNumber.editText?.text?.isEmpty() == true) {
            binding.textInputVehicleNumber.error = "Enter vehicle number"
            return
        }

        val vehicleModel = binding.textInputVehicleModel.editText?.text.toString()
        val vehicleNumber = binding.textInputVehicleNumber.editText?.text.toString()
        profileViewModel.addVehicle(
            Vehicle(
                vehicleModel, vehicleNumber, selectedVehicleType
            )
        )
    }


}