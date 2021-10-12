package com.example.getmyparking.ui.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.getmyparking.adapter.SearchLocationAdapter
import com.example.getmyparking.databinding.FragmentMapBinding
import com.example.getmyparking.interfaces.BottomParkingDialogListener
import com.example.getmyparking.interfaces.SearchLocationAdapterListener
import com.example.getmyparking.ui.MainActivity
import com.example.getmyparking.utils.*
import com.example.getmyparking.utils.Constants.REQUEST_LOCATION_CHECK_SETTINGS
import com.example.getmyparking.utils.Utilities.bitmapDescriptorFromVector
import com.example.getmyparking.utils.Utilities.clearEditTextFocus
import com.example.getmyparking.utils.enums.ParkingErrorCode
import com.example.getmyparking.viewModel.ParkingViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.*
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.maps.android.clustering.ClusterManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import android.os.Handler
import android.os.Looper
import com.example.getmyparking.R
import com.example.getmyparking.utils.Utilities.isNetworkAvailable


@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(), OnMapReadyCallback,
    SearchLocationAdapterListener, OnCameraIdleListener, BottomParkingDialogListener{


    private var mMap: GoogleMap? = null
    private var mLocationRequest:LocationRequest? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var autocompleteFragment: AutocompleteSupportFragment
    private lateinit var mAdapter: SearchLocationAdapter
    private val parkingViewModel:ParkingViewModel by activityViewModels()
    private lateinit var clusterManager: ClusterManager<ClusterMarker>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate:")
        checkInternetConnection()
    }

    private fun checkInternetConnection(){
        if (isNetworkAvailable(requireContext())){
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            locationRequest()
            checkLocationSetting()
        }else{
            showAlertDialog(title = "Network issue!",
                message = "check you internet connection",
                positiveText = "Try again",
                positiveBtnClick = {
                    checkInternetConnection()
                },
                negativeBtnClick = {
                    requireActivity().finish()
                })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentMapBinding.inflate(inflater, container, false)
        Timber.d("onCreateView:")
        setupUI()
        setupObserver()
        return  binding.root
    }

    @SuppressLint("PotentialBehaviorOverride", "MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        Timber.d("onMapReady:")
        if (!Utilities.hasPermissions(requireContext(), MainActivity.permissions.toTypedArray())){
            return
        }

        mMap = googleMap
        setupClusterManager()
        mMap?.apply {
            uiSettings.isMyLocationButtonEnabled = false
            uiSettings.isZoomControlsEnabled = false
            isMyLocationEnabled  = true
            setOnMarkerClickListener(clusterManager)
            setOnCameraIdleListener(this@MapFragment)
        }
        getLastLocation()
    }

    private fun locationRequest(){
        mLocationRequest = LocationRequest.create().apply {
            interval = Constants.LOCATION_UPDATE_INTERVAL
            fastestInterval = Constants.FASTEST_LOCATION_INTERVAL
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun checkLocationSetting() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest!!)
        builder.setAlwaysShow(true)
        val request = builder.build()
        val client = LocationServices.getSettingsClient(requireActivity().applicationContext)

        val locationResponseTask = client.checkLocationSettings(request)
        locationResponseTask.addOnSuccessListener {
            initMap()
        }
            .addOnFailureListener {exception->
                if (exception is ResolvableApiException) {
                    try {
//                        exception.startResolutionForResult(requireActivity(),
//                            REQUEST_LOCATION_CHECK_SETTINGS)
                        startIntentSenderForResult(exception.resolution.intentSender,
                            REQUEST_LOCATION_CHECK_SETTINGS,
                            null, 0, 0, 0, null)
                    } catch (ex: IntentSender.SendIntentException) {
                        ex.printStackTrace()
                    }
                }

            }
    }

    private fun initMap(){

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.frag_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupUI(){

        mAdapter = SearchLocationAdapter(this)
        binding.searchRecycler.adapter = mAdapter

        binding.fragMapFabLocation.setOnClickListener {
            getLastLocation()
        }

        binding.user.setOnClickListener {
            navigateToNextScreen(R.id.action_mapFragment_to_profileFragment)
        }
    }

    private fun setupObserver(){

        binding.fragMapSearchText.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.frameAutosuggestion.visibility = View.VISIBLE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                findParkingForLocation(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })


        parkingViewModel.searchedLocations.observe(viewLifecycleOwner, { response->
            when(response){
                is Resource.Success->{
                    response.data?.let {addressList ->
                        binding.txtNoResultFound.visibility = if (addressList.isEmpty()) View.VISIBLE else View.GONE
                        binding.autosuggestionLoader.visibility = View.GONE
                        mAdapter.submitList(addressList)
                    }
                }

                is Resource.Error->{
                    response.message?.let {
                        binding.frameAutosuggestion.visibility = View.GONE
                        if (!it.contains("grpc failed")){
                            toastMessage(it)
                        }
                    }
                }

                is Resource.Loading->{
                    binding.autosuggestionLoader.visibility = View.VISIBLE
                }
            }
        })

        parkingViewModel.parkingList.observe(viewLifecycleOwner, { response->
            when(response){
                is Resource.Success->{
                    parkingViewModel.setLoadingState(false)
                    response.data?.let {parkingList ->
                        getParkingBetweenBound()
                        Timber.d(" parking observer: Success: ${parkingList.size}")
                    }
                }

                is Resource.Error->{
                    parkingViewModel.setLoadingState(false)
                    val errorDto = response.message?.let { Utilities.deserializeParkingError(it) }
                    Timber.d(" parking observer: error: $errorDto")
                    if (errorDto?.parkingErrorCode == ParkingErrorCode.UNAUTHORIZED_CITY_ACCESS){
                        toastMessage("Currently Parking is not available in your city")
                    }else{
                        toastMessage(errorDto?.message)
                    }
                }

                is Resource.Loading->{
                    Timber.d("parking observer: loading:")
                    parkingViewModel.setLoadingState(true)
                }
            }
        })

        parkingViewModel.loadingState.observe(viewLifecycleOwner){loadingState->
            when(loadingState){
                true-> showLoading()
                false-> hideLoading()
            }
        }

        parkingViewModel.currentViewedParking.observe(viewLifecycleOwner) { list ->
            Timber.d(" current viewed parking observer:")
            list?.let { parkingList ->
                clusterManager.clearItems()
                parkingList.forEach { parking ->
                    val marker = ClusterMarker(
                        lat = parking.latitude,
                        lng = parking.longitude,
                        title = parking.displayName!!,
                        snippet = parking.address!!,
                        isDynamic = parking.isDynamicParking
                    )
                    clusterManager.addItem(marker)
                }
                clusterManager.cluster()
            }
        }
    }

    private fun setupClusterManager(){
        clusterManager = ClusterManager(requireActivity(), mMap)
        clusterManager.renderer = ClusterRenderer(requireContext(), mMap!!, clusterManager)

        clusterManager.setOnClusterClickListener { clusterList->
            Timber.d("onCLusterCLick ${clusterList.size}")
            return@setOnClusterClickListener true
        }
        clusterManager.setOnClusterItemClickListener { clusterMarker->
            Timber.d("onCLusterItemCLick ${clusterMarker.position}")
            parkingViewModel.getSelectedMarkerDetail(clusterMarker.position)
            openBottomParkingDialog()
            return@setOnClusterItemClickListener true
        }
    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        mFusedLocationClient?.lastLocation?.addOnSuccessListener {
            it?.let { location ->
                val latLng = LatLng(location.latitude, location.longitude)
                zoomMapToLocation(latLng)
                findParkingForLocation(latLng)
            }
        }
    }

    private fun zoomMapToLocation(latLng: LatLng){
        mMap?.apply {
            val moveLocation = CameraUpdateFactory.newLatLngZoom(
                latLng, 15f
            )
            animateCamera(moveLocation)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_LOCATION_CHECK_SETTINGS->{
                if(resultCode == Activity.RESULT_OK){
                    initMap()
                }else if(resultCode == Activity.RESULT_CANCELED){
                    toastMessage("Location is required")
                }
            }
        }
    }

    private fun addMarker(latLng: LatLng, markerIcon:Int): Marker? {
        val locationMarker = bitmapDescriptorFromVector(requireContext(), markerIcon)
        return mMap?.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("trial")
                .draggable(false)
                .icon(locationMarker)
        )
    }

    private fun findParkingForLocation(place: String) {
        parkingViewModel.setSearchedLocation(Resource.Loading())
        CoroutineScope(Dispatchers.IO).launch {
            kotlin.runCatching {
                var addresses = ArrayList<Address>()
                try {
                    addresses =
                        Geocoder(requireContext()).getFromLocationName(
                            place,
                            5
                        ) as ArrayList<Address>
                    Timber.d("location: $addresses")
                    parkingViewModel.setSearchedLocation(Resource.Success(data = addresses))
                } catch (e: IOException) {
                    Timber.d("location: $e")
                    parkingViewModel.setSearchedLocation(e.message?.let { Resource.Error(it) })
                    e.printStackTrace()
                }
            }
        }
    }

    private fun findParkingForLocation(latLng: LatLng) = CoroutineScope(Dispatchers.IO).launch {
        kotlin.runCatching {
            val addresses: ArrayList<Address>
            try {
                addresses =
                    Geocoder(requireContext()).getFromLocation(
                        latLng.latitude,
                        latLng.longitude,
                        1
                    ) as ArrayList<Address>
                addresses.first().locality?.let { parkingViewModel.getParkingForCity(it) }
                Timber.d("location: $addresses")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun getParkingBetweenBound(){
        Timber.d("getParkingBetweenBound")
        val bounds = mMap?.projection?.visibleRegion?.latLngBounds
        if (bounds != null) {
            parkingViewModel.getParkingBetweenBound(bounds)
        }
    }

    override fun onSearchResultClick(address: Address) {
        clearEditTextFocus(binding.fragMapSearchText, requireActivity())
        binding.frameAutosuggestion.visibility = View.GONE
        Timber.tag("ApiCheck").d(" onSearchResultCLick: ${address.locality}")
        zoomMapToLocation(
            LatLng(
                address.latitude,
                address.longitude
            )
        )
        Handler(Looper.getMainLooper()).postDelayed({
            address.locality?.let { parkingViewModel.getParkingForCity(it) }
        }, 1500)
    }

    override fun onCameraIdle() {
       getParkingBetweenBound()
    }

    private fun openBottomParkingDialog(){
        val bottomDialog = BottomParkingDetailDialogFragment(this)
        bottomDialog.show(requireActivity().supportFragmentManager, "BottomParkingDialog")
    }

    private fun openServiceBottomDialog(){
        val bottomDialog = ServiceOnWheelsFragment()
        bottomDialog.show(requireActivity().supportFragmentManager, "ServiceOnWheelsDialog")
    }

    override fun onBookNowClick() {
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen(R.id.action_mapFragment_to_bookingFragment)
        }, 200)
    }

    override fun onDetailClick() {
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen(R.id.action_mapFragment_to_parkingDetailFragment)
        }, 200)
    }

    override fun onNavigateClick(position: LatLng?) {
        Handler(Looper.getMainLooper()).postDelayed({
            position?.let { openGoogleMapApplication(it) }
        }, 200)
    }

    override fun onVASClick() {
        Handler(Looper.getMainLooper()).postDelayed({
            openServiceBottomDialog()
        }, 100)
    }
}