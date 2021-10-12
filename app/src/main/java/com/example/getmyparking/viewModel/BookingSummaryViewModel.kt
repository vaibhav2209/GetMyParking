package com.example.getmyparking.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.getmyparking.models.BookingSummary
import com.example.getmyparking.ui.fragments.BookingSummaryFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BookingSummaryViewModel @Inject constructor(

):BaseViewModel() {

    private val _bookingSummary = MutableLiveData<BookingSummary>()
    val bookingSummary:LiveData<BookingSummary> = _bookingSummary


    fun setBookingSummary(bookingSummary: BookingSummary){
        _bookingSummary.value = bookingSummary
    }
}