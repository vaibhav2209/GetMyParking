package com.example.getmyparking.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.getmyparking.R
import com.example.getmyparking.databinding.FragmentBottomParkingDetailDialogBinding
import com.example.getmyparking.viewModel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomParkingDetailDialogFragment : BottomSheetDialogFragment() {

    private var _binding:FragmentBottomParkingDetailDialogBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomParkingDetailDialogBinding.inflate(inflater, container, false)
        setupUI()
        return binding.root
    }

    private fun setupUI(){
        binding.linearGetDirection.setOnClickListener {

        }

        binding.linearParkingInfo.setOnClickListener {
            findNavController().navigate(R.id.action_bottomParkingDetailDialogFragment_to_parkingDetailFragment)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}