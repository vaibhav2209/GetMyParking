package com.example.getmyparking.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.getmyparking.R
import com.example.getmyparking.databinding.FragmentBottomParkingDetailDialogBinding
import com.example.getmyparking.viewModel.ParkingViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.NullPointerException

@AndroidEntryPoint
class BottomParkingDetailDialogFragment : BottomSheetDialogFragment() {

    private var _binding:FragmentBottomParkingDetailDialogBinding? = null
    private val binding get() = _binding!!

    private val mViewModel: ParkingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomParkingDetailDialogBinding.inflate(inflater, container, false)
        setupUI()
        return binding.root
    }

    private fun setupUI(){
        binding.btnNavigate.setOnClickListener {

        }

        binding.linearParkingInfo.setOnClickListener {
            findNavController().navigate(R.id.action_bottomParkingDetailDialogFragment_to_parkingDetailFragment)
        }

        binding.btnBookNow.setOnClickListener {
            findNavController().navigate(R.id.action_bottomParkingDetailDialogFragment_to_bookingFragment)
        }

        binding.btnVAS.setOnClickListener {

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private fun showMap(latLng: LatLng) {
        val gmmIntentUri = Uri.parse("google.navigation:q=" + latLng.latitude + "," + latLng.longitude)

        val intent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
            `package` = "com.google.android.apps.maps"
        }
        try {
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            }
        }catch (e: NullPointerException) {
            Timber.e("NullPointerException: Couldn't open map. ${e.message}")
            Toast.makeText(requireActivity(), "Couldn't open map", Toast.LENGTH_SHORT).show()
        }
    }

}