package com.example.getmyparking.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.getmyparking.adapter.ParkingImageAdapter
import com.example.getmyparking.animations.ImageSlideAnimation
import com.example.getmyparking.data.local.ParkingEntity
import com.example.getmyparking.data.local.ParkingLotEntity
import com.example.getmyparking.databinding.FragmentParkingDetailBinding
import com.example.getmyparking.interfaces.ParkingImagesListener
import com.example.getmyparking.utils.Utilities.driveImageLinkGenerator
import com.example.getmyparking.utils.enums.ParkingLotType
import com.example.getmyparking.viewModel.ParkingViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ParkingDetailFragment : BaseFragment<FragmentParkingDetailBinding>(), ParkingImagesListener{


    private lateinit var imageAdapter: ParkingImageAdapter
    private val parkingViewModel:ParkingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentParkingDetailBinding.inflate(inflater, container, false)
        setupUI()
        setupObserver()
        return binding.root
    }


    private fun setupUI(){
        imageAdapter = ParkingImageAdapter(arrayListOf(), this)
        binding.imageViewPager.apply {
            adapter = imageAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            offscreenPageLimit = 3
            setPageTransformer(ImageSlideAnimation(3))
        }

        binding.btnNavigate.setOnClickListener {
        }

        binding.btnBookNow.setOnClickListener {

        }
    }

    private fun setupObserver(){
        binding.imageViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

            }
        })

        parkingViewModel.selectedParkingDetail.observe(viewLifecycleOwner){
            it?.let {
                setupInfo(it)
            }
        }

    }

    private fun setupInfo(parkingEntity: ParkingEntity) {
        parkingEntity.apply {
            if (!isDynamicParking){
                binding.btnBookNow.visibility = GONE
            }
            binding.areaName.text = displayName
            binding.cityName.text = city
            binding.txtOpeningHours.text = operationalHours?.first()?.openTime
            parkingLots?.forEach { parkingLotEntity ->
                val parkingCost:String = if(isDynamicParking)
                    getParkingPrice(parkingLotEntity)
                else
                    "Free"
                when(parkingLotEntity.type){
                    ParkingLotType.BIKE->{
                        binding.txtBikeCapacity.text = parkingLotEntity.capacity.toString()
                        binding.txtPricingBike.text = parkingCost
                    }
                    ParkingLotType.CAR->{
                        binding.txtCarCapacity.text = parkingLotEntity.capacity.toString()
                        binding.txtPricingCar.text = parkingCost
                    }
                    ParkingLotType.COMMERCIAL->{
                        binding.txtCarCapacity.text = parkingLotEntity.capacity.toString()
                        binding.txtPricingCommercial.text = parkingCost
                    }
                }
            }
            setupParkingImages(parkingEntity)
        }
    }

    private fun setupParkingImages(parkingEntity: ParkingEntity){
        val parkingImagesUrls = ArrayList<String>().apply {
            parkingEntity.entrancePhoto?.let { add(it) }
            parkingEntity.entrancePhotoAdditional?.let { add(it) }
            parkingEntity.areaPhoto?.let { add(driveImageLinkGenerator(it)) }
            parkingEntity.areaPhoto2?.let { add(driveImageLinkGenerator(it)) }
            parkingEntity.extraPhoto?.let { add(driveImageLinkGenerator(it)) }
            parkingEntity.receiptPhoto?.let { add(driveImageLinkGenerator(it)) }
        }
        imageAdapter.submitUrls(parkingImagesUrls)
    }




    private fun getParkingPrice(parkingLotEntity: ParkingLotEntity) =
        StringBuilder()
            .append("1hr ₹${parkingLotEntity.cost1Hour}, ")
            .append("2hr ₹${parkingLotEntity.cost2Hour}, ")
            .append("4hr ₹${parkingLotEntity.cost4Hour}, ")
            .append("8hr ₹${parkingLotEntity.cost8Hour}, ")
            .append("12hr ₹${parkingLotEntity.cost12Hour}, ")
            .append("24r ₹${parkingLotEntity.cost24Hour}")
            .toString()


    override fun onImageClick(position: Int) {

    }
}