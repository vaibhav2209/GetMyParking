package com.example.getmyparking.viewModel

import androidx.lifecycle.MutableLiveData
import com.example.getmyparking.network.WebServiceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
): BaseViewModel() {

    val VasData = MutableLiveData<List<String>>()


}