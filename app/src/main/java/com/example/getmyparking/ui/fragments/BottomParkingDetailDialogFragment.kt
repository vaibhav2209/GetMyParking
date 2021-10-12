package com.example.getmyparking.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.getmyparking.R
import com.example.getmyparking.data.local.ParkingEntity
import com.example.getmyparking.databinding.FragmentBottomParkingDetailDialogBinding
import com.example.getmyparking.interfaces.BottomParkingDialogListener
import com.example.getmyparking.viewModel.ParkingViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class BottomParkingDetailDialogFragment(
    private val listener:BottomParkingDialogListener
): BottomSheetDialogFragment() {

    private var _binding:FragmentBottomParkingDetailDialogBinding? = null
    private val binding get() = _binding!!
    private var position:LatLng? = null

    private val parkingViewModel: ParkingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.tag("BottomFragment").d("onCreate:")
        _binding = FragmentBottomParkingDetailDialogBinding.inflate(inflater, container, false)
        setupUI()
        setupObserver()
        return binding.root
    }

    private fun setupUI(){
        binding.btnNavigate.setOnClickListener {
            listener.onNavigateClick(position)
            dialog?.dismiss()
        }

        binding.linearParkingInfo.setOnClickListener {
            listener.onDetailClick()
            dialog?.dismiss()
        }

        binding.btnBookNow.setOnClickListener {
            listener.onBookNowClick()
            dialog?.dismiss()
        }

        binding.btnVAS.setOnClickListener {
            listener.onVASClick()
        }

    }

    private fun setupObserver(){
        parkingViewModel.selectedParkingDetail.observe(viewLifecycleOwner){
            it?.let { parkingEntity ->
                setDetails(parkingEntity)
            }
        }
    }

    private fun setDetails(parkingEntity: ParkingEntity){
        if(!parkingEntity.isDynamicParking){
            binding.btnBookNow.visibility = GONE
            binding.btnVAS.visibility = GONE
            binding.parkingIcon.setImageResource(R.drawable.ic_parking_marker)
        }
        binding.areaName.text = parkingEntity.displayName
        binding.cityName.text = parkingEntity.city
        position = LatLng(parkingEntity.latitude, parkingEntity.longitude)
    }

    override fun onDestroyView() {
        Timber.tag("BottomFragment").d("onDestroyVIew:")
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.tag("BottomFragment").d("onDestroy:")
        _binding = null
    }

}