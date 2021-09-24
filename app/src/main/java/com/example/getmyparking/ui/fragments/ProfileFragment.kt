package com.example.getmyparking.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.getmyparking.R
import com.example.getmyparking.adapter.UserVehiclesAdapter
import com.example.getmyparking.databinding.FragmentProfileBinding
import com.example.getmyparking.interfaces.UserVehicleAdapterListener
import com.example.getmyparking.models.Vehicle
import com.example.getmyparking.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(), UserVehicleAdapterListener {

    private lateinit var mAdapter:UserVehiclesAdapter
    private val profileViewModel:ProfileViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        setupUI()
        setupObserver()
        return binding.root
    }

    private fun setupUI(){
        binding.cardPersonalDetail.setOnClickListener { showUserDetail() }
        binding.imgBtnAdd.setOnClickListener { addNewVehicle() }
        binding.backBtn.setOnClickListener { findNavController().popBackStack() }

        mAdapter = UserVehiclesAdapter(arrayListOf(), this)
        binding.recyclerVehicals.adapter = mAdapter
        binding.recyclerVehicals.setHasFixedSize(true)
        binding.recyclerVehicals.itemAnimator = DefaultItemAnimator()
    }

    private fun setupObserver(){
        profileViewModel.vehicleData.observe(viewLifecycleOwner, {
            it?.let {vehicleList->
                mAdapter.submitList(vehicleList)
            }
        })
    }

    private fun addNewVehicle() {
        navigateToNextScreen(R.id.action_profileFragment_to_addVehicleFragment)
    }

    private fun showUserDetail() {

    }

    override fun onVehicleClick(vehicle: Vehicle) {

    }
}