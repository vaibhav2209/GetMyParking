package com.example.getmyparking.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.getmyparking.R
import com.example.getmyparking.data.local.CarEntity
import com.example.getmyparking.databinding.FragmentAddVehicleBinding
import com.example.getmyparking.utils.Utilities
import com.example.getmyparking.utils.enums.ParkingLotType
import com.example.getmyparking.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import android.text.InputFilter

import android.text.Spanned




@AndroidEntryPoint
class AddVehicleFragment : BaseFragment<FragmentAddVehicleBinding>() {

    private val profileViewModel:ProfileViewModel by activityViewModels()
    private var isEdit = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentAddVehicleBinding.inflate(inflater, container, false)
        setupUI()
        setUpObserver()
        return binding.root
    }

    private fun setupUI(){
        binding.btnAddVehicleDone.setOnClickListener {
            if(validateVehicleData()){
                Utilities.hideKeyBoard(requireActivity())
                findNavController().popBackStack()
            }
        }
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, ParkingLotType.values())
        binding.autoCompleteVehicleType.setAdapter(adapter)

        binding.backBtn.setOnClickListener {
            profileViewModel.setEditVehicle(isEdit = false)
            Utilities.hideKeyBoard(requireActivity())
            findNavController().popBackStack()
        }
        /*val filter =
            InputFilter { source, start, end, dest, dstart, dend ->
                source.forEach {  c ->
                    Timber.d("Text: $c")
                    if (!Character.isLetterOrDigit(c)) {
                        Timber.d("Text if: $c")
                        return@InputFilter ""
                    }
                }
                Timber.d("Text: null")
                null
            }
        binding.editTxtVehicleNumber.filters = arrayOf(filter)*/
    }

    private fun setUpObserver(){
        profileViewModel.isEditing.observe(viewLifecycleOwner){
            it?.let {
                isEdit = it.first
                if (it.first){
                   it.second?.let { id->
                       profileViewModel.getVehicleById(id)
                   }
                }
            }
        }

        profileViewModel.editVehicle.observe(viewLifecycleOwner){car->
            car?.let {entity->
                if (isEdit){
                    binding.editTxtVehicleModel.setText(entity.model)
                    binding.editTxtVehicleNumber.setText(entity.number)
                    binding.autoCompleteVehicleType.setText(entity.type.toString(), false)
                }
            }
        }

    }

    private fun validateVehicleData(): Boolean {
        if (binding.editTxtVehicleModel.text?.isEmpty() == true) {
            binding.textInputVehicleModel.error = "Enter vehicle model"
            return false
        }
        if (binding.editTxtVehicleNumber.text?.isEmpty() == true) {
            binding.editTxtVehicleNumber.error = "Enter vehicle number"
            return false
        }
        val check = binding.editTxtVehicleNumber.text?.any {
            !Character.isLetterOrDigit(it)
        }
        if (check == true){
            binding.editTxtVehicleNumber.error = "Invalid Car Number"
            return false
        }

        val vehicleModel = binding.editTxtVehicleModel.text.toString()
        val vehicleNumber = binding.editTxtVehicleNumber.text.toString()
        val type = binding.autoCompleteVehicleType.text.toString()
        val vehicle = CarEntity(
            model = vehicleModel,
            number = vehicleNumber,
            type = ParkingLotType.valueOf(type)
        )

        Timber.d("isEdit: $isEdit ${vehicle.id}")
        if (isEdit){
            profileViewModel.editVehicle(vehicle)
        }else{
            profileViewModel.addVehicle(vehicle)
        }
        profileViewModel.setEditVehicle(isEdit = false)

        return true
    }

}