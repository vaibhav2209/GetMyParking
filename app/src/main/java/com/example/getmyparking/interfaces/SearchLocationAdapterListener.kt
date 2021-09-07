package com.example.getmyparking.interfaces

import android.location.Address

interface SearchLocationAdapterListener {
    fun onSearchResultClick(address: Address)
}