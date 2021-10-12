package com.example.getmyparking.ui.fragments

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.NullPointerException


open class BaseFragment<K:ViewBinding> : Fragment() {

    protected var _binding:K? = null
    protected val binding get() = _binding!!

    private val loadingDialogFragment by lazy { LoadingDialogFragment() }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    protected open fun toastMessage(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    protected fun navigateToNextScreen(navigationId: Int, bundle: Bundle? = null){
        findNavController().navigate(navigationId, bundle)
    }

    fun openGoogleMapApplication(latLng: LatLng) {
        val gmmIntentUri = Uri.parse("google.navigation:q=" + latLng.latitude + "," + latLng.longitude)

        val intent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
            `package` = "com.google.android.apps.maps"
        }
        try {
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            }
        }catch (e: NullPointerException) {
            Timber.e("NullPointerException: Couldn't open map. ${e.message}")
            toastMessage("Couldn't open map")
        }
    }

    fun showLoading(){
        if (!loadingDialogFragment.isAdded){
            loadingDialogFragment.show(childFragmentManager, "loader")
        }
    }

    fun hideLoading(){
        if (loadingDialogFragment.isAdded) {
            loadingDialogFragment.dismissAllowingStateLoss()
        }
    }

    fun showAlertDialog(
        positiveText:String = "Okay",
        negativeBtnText:String = "Cancel",
        title:String,
        message: String,
        isCancelable:Boolean = true,
        positiveBtnClick:() -> Unit,
        negativeBtnClick: () -> Unit = { }
    ){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setCancelable(isCancelable)
            .setPositiveButton(positiveText){dialog, which->
                positiveBtnClick()
                dialog.dismiss()
            }
            .setNegativeButton(negativeBtnText){dialog, which->
                negativeBtnClick()
                dialog.dismiss()
            }
            .show()
    }
}