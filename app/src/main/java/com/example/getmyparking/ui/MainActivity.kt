package com.example.getmyparking.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.getmyparking.BaseActivity
import com.example.getmyparking.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
    }



}