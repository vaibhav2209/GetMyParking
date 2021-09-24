package com.example.getmyparking.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.getmyparking.R
import com.example.getmyparking.databinding.FragmentBottomParkingDetailDialogBinding
import com.example.getmyparking.databinding.FragmentServiceOnWheelsBinding
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ServiceOnWheelsFragment : BottomSheetDialogFragment() {


    private var _binding: FragmentServiceOnWheelsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServiceOnWheelsBinding.inflate(inflater, container, false)
        setupUI()
        return binding.root
    }


    private fun setupUI(){
        binding.closeBtn.setOnClickListener {
            dialog?.dismiss()
        }
    }

}