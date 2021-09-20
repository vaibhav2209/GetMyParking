package com.example.getmyparking.utils

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class ClusterMarker(
    lat: Double,
    lng: Double,
    private val title: String,
    private val snippet: String,
    val isDynamic:Boolean
): ClusterItem {

    private val position: LatLng = LatLng(lat, lng)

    override fun getPosition(): LatLng {
        return position
    }

    override fun getTitle(): String {
        return  title
    }

    override fun getSnippet(): String {
        return snippet
    }

}