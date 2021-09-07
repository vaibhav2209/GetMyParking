package com.example.getmyparking.ui.fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.getmyparking.R
import com.example.getmyparking.databinding.FragmentSplashScreenBinding
import com.example.getmyparking.utils.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.example.getmyparking.utils.Utilities
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted

class SplashScreenFragment : BaseFragment<FragmentSplashScreenBinding>(), EasyPermissions.PermissionCallbacks {


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        Handler(Looper.getMainLooper()).postDelayed({
            requestPermissions()
        }, 1500)
        return binding.root
    }


    @AfterPermissionGranted(REQUEST_CODE_LOCATION_PERMISSION)
    private fun requestPermissions(){
        if (Utilities.hasLocationPermissions(requireContext())){
            navigateToNextScreen(R.id.action_splashScreenFragment_to_mapFragment)
            return
        }

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                 REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }


    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        navigateToNextScreen(R.id.action_splashScreenFragment_to_mapFragment)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
    


}