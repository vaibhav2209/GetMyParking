package com.example.getmyparking.ui.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import coil.ImageLoader
import coil.request.ImageRequest
import coil.util.CoilUtils
import com.example.getmyparking.R
import com.example.getmyparking.adapter.ParkingImageAdapter
import com.example.getmyparking.animations.ImageSlideAnimation
import com.example.getmyparking.data.local.ParkingEntity
import com.example.getmyparking.data.local.ParkingLotEntity
import com.example.getmyparking.databinding.FragmentParkingDetailBinding
import com.example.getmyparking.interfaces.ParkingImagesListener
import com.example.getmyparking.utils.Utilities
import com.example.getmyparking.utils.Utilities.driveImageLinkGenerator
import com.example.getmyparking.utils.enums.ParkingLotType
import com.example.getmyparking.viewModel.ParkingViewModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import timber.log.Timber

@AndroidEntryPoint
class ParkingDetailFragment : BaseFragment<FragmentParkingDetailBinding>(), ParkingImagesListener{


    private lateinit var imageAdapter: ParkingImageAdapter
    private val parkingViewModel:ParkingViewModel by activityViewModels()
    private var position:LatLng? = null

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
            position?.let {
                openGoogleMapApplication(it)
            }
        }

        binding.btnBookNow.setOnClickListener {
            navigateToNextScreen(R.id.action_parkingDetailFragment_to_bookingFragment)
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
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
        position = LatLng(parkingEntity.latitude, parkingEntity.longitude)
        parkingEntity.apply {
            if (!isDynamicParking){
                binding.btnBookNow.visibility = GONE
                binding.linearVAS.visibility = GONE
                binding.parkingIcon.setImageResource(R.drawable.ic_parking_marker)
            }
            binding.areaName.text = displayName
            binding.cityName.text = city
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

            binding.ticketingSystem.text = ticketingSystem?.toString()
            binding.cctv.text = cctvInstalled?.toString()
            binding.powerSupply.text = powerSupplyAvailable?.toString()
            binding.structure.text = structure?.name
            binding.ownership.text = ownership?.name
            if (paymentMethods.isNotEmpty()){
                binding.txtPaymentType.text = paymentMethods.toString().drop(1).dropLast(1)
            }
            operationalHours?.first()?.apply {
                val startTime = openTime?.let { openTime->
                    return@let Utilities.openingTimeFormatter(openTime)
                }
                val endTime = closeTime?.let { closeTime->
                    return@let Utilities.openingTimeFormatter(closeTime)
                }
                if (startTime != null && endTime != null){
                    binding.txtOpeningHours.text = StringBuilder().append(startTime).append("-").append(endTime).toString()
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
            //parkingEntity.extraPhoto?.let { add(driveImageLinkGenerator(it)) }
            parkingEntity.receiptPhoto?.let { add(driveImageLinkGenerator(it)) }
        }
        checkImage(parkingImagesUrls)
    }

    private fun checkImage(urlList:List<String>) = CoroutineScope(Dispatchers.IO).launch{
        val drawableList = arrayListOf<Drawable>()
        val imageLoader = ImageLoader.Builder(requireContext())
            .okHttpClient {
                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(requireContext()))
                    .build()
            }
            .build()

        urlList.forEach {
            val request = ImageRequest.Builder(requireContext())
                .data(it)
                .target (
                    onSuccess = { result ->
                        binding.lottieImgLoading.visibility = GONE
                        imageAdapter.submitUrls(result)
                        drawableList.add(result)
                        Timber.d("onSuccess:")
                    },
                    onError = {
                        Timber.d("onError:")
                    },
                    onStart = {
                        Timber.d("onStart:")
                    }
                )
                .build()
            imageLoader.execute(request)
        }
        if (drawableList.isEmpty()) {
            withContext(Dispatchers.Main) {
                ContextCompat.getDrawable(requireContext(), R.drawable.img_parking_place)?.let {
                    imageAdapter.submitUrls(it)
                    binding.lottieImgLoading.visibility = GONE
                }
            }
        }

    }

    private fun getParkingPrice(parkingLotEntity: ParkingLotEntity) =
        StringBuilder()
            .append("1hr ???${parkingLotEntity.cost1Hour}, ")
            .append("2hr ???${parkingLotEntity.cost2Hour}, ")
            .append("4hr ???${parkingLotEntity.cost4Hour}, ")
            .append("8hr ???${parkingLotEntity.cost8Hour}, ")
            .append("12hr ???${parkingLotEntity.cost12Hour}, ")
            .append("24r ???${parkingLotEntity.cost24Hour}")
            .toString()


    override fun onImageClick(position: Int) {

    }
}