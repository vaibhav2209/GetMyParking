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
import androidx.fragment.app.viewModels
import com.example.getmyparking.R
import com.example.getmyparking.adapter.SearchLocationAdapter
import com.example.getmyparking.databinding.FragmentMapBinding
import com.example.getmyparking.interfaces.SearchLocationAdapterListener
import com.example.getmyparking.utils.Constants
import com.example.getmyparking.utils.Constants.REQUEST_LOCATION_CHECK_SETTINGS
import com.example.getmyparking.utils.Resource
import com.example.getmyparking.utils.Utilities
import com.example.getmyparking.utils.Utilities.bitmapDescriptorFromVector
import com.example.getmyparking.utils.Utilities.clearEditTextFocus
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
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.IOException

@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(), OnMapReadyCallback,
    OnMarkerClickListener, OnCameraMoveStartedListener, OnMapClickListener,
    OnMyLocationClickListener, OnMyLocationButtonClickListener,
    SearchLocationAdapterListener {


    private var mMap: GoogleMap? = null
    private var mLocationRequest:LocationRequest? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var autocompleteFragment: AutocompleteSupportFragment
    private lateinit var mAdapter: SearchLocationAdapter
    private val parkingViewModel:ParkingViewModel by viewModels()

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
        if (!Utilities.hasLocationPermissions(requireContext())){
            return
        }

        mMap = googleMap ?: return
        mMap?.apply {
            uiSettings.isMyLocationButtonEnabled = true
            uiSettings.isZoomControlsEnabled = false
            isMyLocationEnabled  = true
            setOnMarkerClickListener(this@MapFragment)
            setOnCameraMoveStartedListener(this@MapFragment)
            setOnMapClickListener(this@MapFragment)
            setOnMyLocationClickListener(this@MapFragment)
            setOnMyLocationButtonClickListener(this@MapFragment)
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

        /*autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))*/
    }

    private fun setupObserver(){
        /*binding.fragMapSearchText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                ||actionId == EditorInfo.IME_ACTION_DONE
                ||actionId == EditorInfo.IME_ACTION_SEND
                ||event.action == KeyEvent.ACTION_DOWN
                ||event.action == KeyEvent.KEYCODE_ENTER){

                findPlace(binding.fragMapSearchText.text.toString())
            }
            return@setOnEditorActionListener false
        }*/
        parkingViewModel.getParkingOfCity(10,1,"bengaluru")

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
                       mAdapter.submitList(addressList)
                    }
               }

               is Resource.Error->{
                   response.message?.let {
                        toastMessage(it)
                   }
               }

               is Resource.Loading->{

               }
           }
        })

        parkingViewModel.parkingList.observe(viewLifecycleOwner, { response->
            when(response){
                is Resource.Success->{
                    response.data?.let {parkingList ->
                       Timber.tag("ApiCheck").d(" parking observer: Success: $parkingList")
                    }
                }

                is Resource.Error->{
                    Timber.tag("ApiCheck").d(" parking observer: error: ${response.message}")
                }

                is Resource.Loading->{
                    Timber.tag("ApiCheck").d("parking observer: loading:")
                }
            }
        })


        /*autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                Timber.d("Searched Places: latLng: ${place.latLng}  address: ${place.address}")
            }

            override fun onError(status: Status) {
                Timber.e("error: ${status.statusMessage}")
            }
        })*/
    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        mFusedLocationClient?.lastLocation?.addOnSuccessListener {
            it?.let { location ->
                val latLng = LatLng(location.latitude, location.longitude)
                zoomMapToLocation(latLng)
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


    private fun addMarker(latLng: LatLng, markerIcon:Int){
        val locationMarker = bitmapDescriptorFromVector(requireContext(), markerIcon)
        mMap?.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("trial")
                .draggable(false)
                .icon(locationMarker)
        )
    }

    private fun findPlace(place: String){
        parkingViewModel.setSearchedLocation(Resource.Loading())
        var addresses = ArrayList<Address>()
        try {
            addresses = Geocoder(requireContext()).getFromLocationName(place, 5) as ArrayList<Address>
        } catch (e: IOException) {
            parkingViewModel.setSearchedLocation(e.message?.let { Resource.Error(it) })
            e.printStackTrace()
        }

        parkingViewModel.setSearchedLocation(Resource.Success(data = addresses))
    }

    override fun onSearchResultClick(address: Address) {
        clearEditTextFocus(binding.fragMapSearchText, requireActivity())
        binding.searchRecycler.visibility = View.VISIBLE
        zoomMapToLocation(
            LatLng(
                address.latitude,
                address.longitude
            )
        )
    }

    override fun onMarkerClick(marker: Marker): Boolean {

        navigateToNextScreen(R.id.action_mapFragment_to_bottomParkingDetailDialogFragment)
        return true
    }

    override fun onCameraMoveStarted(p0: Int) {
    }

    override fun onMapClick(latLng: LatLng) {
        addMarker(latLng,R.drawable.ic_parking_marker)
    }

    override fun onMyLocationClick(p0: Location) {

    }

    override fun onMyLocationButtonClick(): Boolean {

        return false
    }
}