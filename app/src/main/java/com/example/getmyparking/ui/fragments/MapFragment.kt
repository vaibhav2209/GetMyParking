package com.example.getmyparking.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.getmyparking.R
import com.example.getmyparking.databinding.FragmentMapBinding
import com.example.getmyparking.utils.Constants
import com.example.getmyparking.utils.Constants.REQUEST_LOCATION_CHECK_SETTINGS
import com.example.getmyparking.utils.Utilities
import com.example.getmyparking.utils.Utilities.bitmapDescriptorFromVector
import com.example.getmyparking.viewModel.MainViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.Status
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
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MapFragment : BaseFragment<MainViewModel, FragmentMapBinding>(), OnMapReadyCallback,
    OnMarkerClickListener, OnCameraMoveStartedListener, OnMapClickListener, OnMyLocationClickListener, OnMyLocationButtonClickListener {


    private var mMap: GoogleMap? = null
    private var mLocationRequest:LocationRequest? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var autocompleteFragment: AutocompleteSupportFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))*/
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
        binding.fragMapFabLocation.setOnClickListener {
            getLastLocation()
        }
    }

    private fun setupObserver(){

        binding.fragMapSearchText.addTextChangedListener(object :TextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

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
                    toastMessage("Location is required", Toast.LENGTH_SHORT)
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

    private fun searchPlace(){

    }


    override fun onMarkerClick(p0: Marker): Boolean {
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