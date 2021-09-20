package com.example.getmyparking.ui.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.getmyparking.R
import com.example.getmyparking.adapter.SearchLocationAdapter
import com.example.getmyparking.databinding.FragmentMapBinding
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
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(), OnMapReadyCallback,
    OnMarkerClickListener, OnCameraMoveStartedListener, OnMapClickListener,
    OnMyLocationClickListener, OnMyLocationButtonClickListener,
    SearchLocationAdapterListener, OnCameraMoveListener, OnCameraIdleListener{


    private var mMap: GoogleMap? = null
    private var mLocationRequest:LocationRequest? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var autocompleteFragment: AutocompleteSupportFragment
    private lateinit var mAdapter: SearchLocationAdapter
    private val parkingViewModel:ParkingViewModel by activityViewModels()
    private lateinit var clusterManager: ClusterManager<ClusterMarker>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        locationRequest()
        checkLocationSetting()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentMapBinding.inflate(inflater, container, false)
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
            uiSettings.isMyLocationButtonEnabled = true
            uiSettings.isZoomControlsEnabled = false
            isMyLocationEnabled  = true
            setOnMarkerClickListener(clusterManager)
            setOnCameraMoveStartedListener(this@MapFragment)
            setOnMapClickListener(this@MapFragment)
            setOnMyLocationClickListener(this@MapFragment)
            setOnMyLocationButtonClickListener(this@MapFragment)
            setOnCameraMoveListener(this@MapFragment)
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

        /*autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))*/
    }

    private fun setupObserver(){

        binding.fragMapSearchText.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.searchRecycler.visibility = View.VISIBLE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                findPlace(s.toString())
            }
        })


        parkingViewModel.searchedLocations.observe(viewLifecycleOwner, { response->
           when(response){
               is Resource.Success->{
                    response.data?.let {addressList ->
                        Timber.tag("ApiCheck").d(" searchLocation observer:success")
                        mAdapter.submitList(addressList)
                    }
               }

               is Resource.Error->{
                   Timber.tag("ApiCheck").d(" searchLocation observer:error")
                   response.message?.let {
                        toastMessage(it)
                   }
               }

               is Resource.Loading->{
                   Timber.tag("ApiCheck").d(" searchLocation observer:loading")
               }
           }
        })

        parkingViewModel.parkingList.observe(viewLifecycleOwner, { response->
            when(response){
                is Resource.Success->{
                    parkingViewModel.setLoadingState(false)
                    response.data?.let {parkingList ->
                       Timber.tag("ApiCheck").d(" parking observer: Success: ${parkingList.size}")
                    }
                }

                is Resource.Error->{
                    parkingViewModel.setLoadingState(false)
                    val errorDto = response.message?.let { Utilities.deserializeParkingError(it) }
                    Timber.tag("ApiCheck").d(" parking observer: error: $errorDto")
                    if (errorDto?.parkingErrorCode == ParkingErrorCode.UNAUTHORIZED_CITY_ACCESS){
                        toastMessage("Currently Parking is not available in your city")
                    }else{
                        toastMessage(errorDto?.message)
                    }
                }

                is Resource.Loading->{
                    Timber.tag("ApiCheck").d("parking observer: loading:")
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
            Timber.tag("ApiCheck").d(" current viewed parking observer:")
            //mMap?.clear()
            list?.let { parkingList ->
                val markerList = ArrayList<Marker>()
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
//                    val markerIcon = if (parking.isDynamicParking)
//                        R.drawable.ic_dynamic_parking_marker
//                    else
//                        R.drawable.ic_parking_marker
//
//                    val marker = addMarker(LatLng(parking.latitude, parking.longitude),markerIcon)
//                    marker?.let {
//                        it.tag = parking.id
//                        markerList.add(it)
//                    }
//                }
//                parkingViewModel.addMarkers(markerList)
                }
                clusterManager.cluster()
            }
        }

        /*autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                Timber.d("Searched Places: latLng: ${place.latLng}  address: ${place.address}")
            }

            override fun onError(status: Status) {
                Timber.e("error: ${status.statusMessage}")
            }
        })*/
    }

    private fun setupClusterManager(){
        clusterManager = ClusterManager(requireActivity(), mMap)
        clusterManager.renderer = ClusterRenderer(requireContext(), mMap!!, clusterManager)

        clusterManager.setOnClusterClickListener { clusterList->
            Timber.tag("clustering").d("onCLusterCLick ${clusterList.size}")
            return@setOnClusterClickListener true
        }
        clusterManager.setOnClusterItemClickListener { clusterMarker->
            Timber.tag("clustering").d("onCLusterItemCLick ${clusterMarker.position}")
            parkingViewModel.getSelectedMarkerDetail(clusterMarker.position)
            navigateToNextScreen(R.id.action_mapFragment_to_bottomParkingDetailDialogFragment)
            return@setOnClusterItemClickListener true
        }
    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        mFusedLocationClient?.lastLocation?.addOnSuccessListener {
            it?.let { location ->
                val latLng = LatLng(location.latitude, location.longitude)
                zoomMapToLocation(latLng)
                findParkingForCurrentLocation(latLng)
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

    private fun findPlace(place: String) {
        Timber.tag("ApiCheck").d("findplace")
        CoroutineScope(Dispatchers.IO).launch {
            kotlin.runCatching {
                parkingViewModel.setSearchedLocation(Resource.Loading())
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
                    parkingViewModel.setSearchedLocation(e.message?.let { Resource.Error(it) })
                    e.printStackTrace()
                }
            }
        }
    }

    private fun findParkingForCurrentLocation(latLng: LatLng) = CoroutineScope(Dispatchers.IO).launch {
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

    override fun onSearchResultClick(address: Address) {
        clearEditTextFocus(binding.fragMapSearchText, requireActivity())
        binding.searchRecycler.visibility = View.VISIBLE
        Timber.tag("ApiCheck").d(" onSearchResultCLick: ${address.locality}")
        address.locality?.let { parkingViewModel.getParkingForCity(it) }
        zoomMapToLocation(
            LatLng(
                address.latitude,
                address.longitude
            )
        )
    }

    override fun onMarkerClick(marker: Marker): Boolean {
//        parkingViewModel.getSelectedMarkerDetail(marker)
//        navigateToNextScreen(R.id.action_mapFragment_to_bottomParkingDetailDialogFragment)
        return true
    }

    override fun onCameraMoveStarted(p0: Int) {

    }



    override fun onMapClick(latLng: LatLng) {

    }

    override fun onMyLocationClick(p0: Location) {

    }

    override fun onMyLocationButtonClick(): Boolean {
        return false
    }

    override fun onCameraMove() {
    }

    override fun onCameraIdle() {
        val bounds = mMap?.projection?.visibleRegion?.latLngBounds
        if (bounds != null) {
            parkingViewModel.getParkingBetweenBound(bounds)
        }
    }
}