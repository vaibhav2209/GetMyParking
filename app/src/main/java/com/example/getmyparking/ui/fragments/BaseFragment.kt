package com.example.getmyparking.ui.fragments

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding


open class BaseFragment<T : ViewModel, K:ViewBinding> : Fragment() {
    protected lateinit var mViewModel:T
    protected var _binding:K? = null
    protected val binding get() = _binding!!


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    protected open fun toastMessage(message: String?, toastTiming: Int) {
        Toast.makeText(requireContext(), message, toastTiming).show()
    }

    fun navigateToNextScreen(navigationId: Int){
        findNavController().navigate(navigationId)
    }
}