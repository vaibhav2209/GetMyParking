package com.example.getmyparking.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.getmyparking.R
import com.example.getmyparking.databinding.FragmentProfileBinding
import com.example.getmyparking.viewModel.MainViewModel
import com.example.getmyparking.viewModel.ProfileViewModel


class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        setupUI()
        return binding.root
    }

    private fun setupUI(){
        binding.cardPersonalDetail.setOnClickListener { showUserDetail() }
        binding.imgBtnAdd.setOnClickListener { addNewVehicle() }
    }

    private fun addNewVehicle() {
        navigateToNextScreen(R.id.action_profileFragment_to_addVehicleFragment)
    }

    private fun showUserDetail() {

    }


}