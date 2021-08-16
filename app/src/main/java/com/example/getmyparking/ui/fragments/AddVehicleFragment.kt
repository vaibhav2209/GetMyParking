package com.example.getmyparking.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.getmyparking.R
import com.example.getmyparking.databinding.FragmentAddVehicleBinding
import com.example.getmyparking.viewModel.MainViewModel
import com.example.getmyparking.viewModel.ProfileViewModel
import timber.log.Timber


class AddVehicleFragment : BaseFragment<ProfileViewModel, FragmentAddVehicleBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentAddVehicleBinding.inflate(inflater, container, false)
        setupUI()
        return binding.root
    }

    private fun setupUI(){
        mViewModel.parkingData.observe(viewLifecycleOwner, Observer{
            Timber.d("parking Data: ${it.data}")
        })
    }


}