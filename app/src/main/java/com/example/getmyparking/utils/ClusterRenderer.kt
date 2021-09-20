package com.example.getmyparking.utils

import android.content.Context
import com.example.getmyparking.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class ClusterRenderer(
    private val context: Context,
    private val mMap: GoogleMap,
    private val clusterManager: ClusterManager<ClusterMarker>
): DefaultClusterRenderer<ClusterMarker>(context, mMap, clusterManager)  {

    override fun onBeforeClusterItemRendered(item: ClusterMarker, markerOptions: MarkerOptions) {
        val markerIcon:Int = if (item.isDynamic){
            R.drawable.ic_dynamic_parking_marker
        }else{
            R.drawable.ic_parking_marker
        }
        markerOptions.icon(
            Utilities.bitmapDescriptorFromVector(
                context,
                markerIcon))
            .title(item.title)

    }


}