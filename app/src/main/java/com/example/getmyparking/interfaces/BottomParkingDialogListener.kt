package com.example.getmyparking.interfaces

import com.google.android.gms.maps.model.LatLng

interface BottomParkingDialogListener {
    fun onBookNowClick()
    fun onDetailClick()
    fun onNavigateClick(position: LatLng?)
    fun onVASClick()
}