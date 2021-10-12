package com.example.getmyparking.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.getmyparking.databinding.FragmentBookingSummaryBinding
import com.example.getmyparking.viewModel.BookingSummaryViewModel
import com.example.getmyparking.viewModel.ParkingViewModel
import timber.log.Timber

class BookingSummaryFragment : BaseFragment<FragmentBookingSummaryBinding>() {


    private val parkingViewModel: ParkingViewModel by activityViewModels()
    private val bookingSummaryViewModel:BookingSummaryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentBookingSummaryBinding.inflate(inflater, container, false)
        setupUI()
        setupObserver()
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//       val arg = BookingSummaryFragmentArgs.fromBundle(requireArguments()).bookingSummary
//        startTime = arg?.get(0)
//        endTime = arg?.get(1)
//    }

    private fun setupUI(){
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnPay.setOnClickListener {
            toastMessage("Successfully Booked")
        }
    }

    private fun setupObserver(){
        parkingViewModel.selectedParkingDetail.observe(viewLifecycleOwner){
            it?.let {
                it.apply {
                    binding.txtParkingName.text = displayName
                    binding.txtParkingAddress.text = address
                }
            }
        }
        bookingSummaryViewModel.bookingSummary.observe(viewLifecycleOwner){
            Timber.d("bookingSummary: $it")
            it?.apply {
                binding.txtEndTime.text = endTime
                binding.txtStartTime.text = startTime
                binding.txtTotalTime.text = "$totalTime hours"
                binding.txtTotalPrice.text = "₹ ${totalPrice.toString()}"
                binding.txtCarNo.text = carNo
                val service = services.map { service-> service.serviceName }
                if (service.isNotEmpty()){
                    binding.txtServices.text = service.toString().drop(1).dropLast(1)
                }
            }
        }
    }

}