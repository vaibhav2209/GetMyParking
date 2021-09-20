package com.example.getmyparking.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.getmyparking.R
import com.example.getmyparking.databinding.FragmentSplashScreenBinding
import com.example.getmyparking.ui.MainActivity
import com.example.getmyparking.utils.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.example.getmyparking.utils.Utilities
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import timber.log.Timber
import android.content.Intent
import android.net.Uri
import android.provider.Settings


class SplashScreenFragment : BaseFragment<FragmentSplashScreenBinding>() {


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        Handler(Looper.getMainLooper()).postDelayed({
            checkPermissions(MainActivity.permissions)
        }, 1000)
        return binding.root
    }

    private fun checkPermissions(permissions: ArrayList<String>) {
        when {
            Utilities.hasPermissions(requireContext(), permissions.toTypedArray()) -> {
                navigateToNextScreen(R.id.action_splashScreenFragment_to_mapFragment)
            }
            else -> {
                mResultPermissionRequest.launch(permissions.toTypedArray())
            }
        }
    }

    private val mResultPermissionRequest = registerForActivityResult((ActivityResultContracts.RequestMultiplePermissions())
    ) { permissions ->
        val deniedPermissions = arrayListOf<String>()

        permissions.entries.forEach {
            if (!it.value)
                deniedPermissions.add(it.key)
        }
        if (deniedPermissions.isEmpty()){
            navigateToNextScreen(R.id.action_splashScreenFragment_to_mapFragment)
        }else{
            shouldShowRationale(deniedPermissions)
        }
    }

    private fun shouldShowRationale(permissions: ArrayList<String>){

        val shouldRequest = permissions.all{shouldShowRequestPermissionRationale(it)}
        if (shouldRequest){
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Permission Required")
                .setMessage("Location permission is required to ren this app")
                .setNegativeButton("Deny") { dialog, _ ->
                    dialog.dismiss()
                    requireActivity().finish()
                }
                .setPositiveButton("Allow") { _, _ ->
                    checkPermissions(permissions)
                }
                .show()
        }
        else{
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Permanently Denied")
                .setMessage("Some of permissions are permanently denied.")
                .setNegativeButton("Deny") { dialog, _ ->
                    dialog.dismiss()
                    requireActivity().finish()
                }
                .setPositiveButton("Turn on") { _, _ ->
                    openAppSetting()
                }
                .show()
        }
    }

    private fun openAppSetting(){
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.data = uri
        requireActivity().startActivity(intent)
    }




}