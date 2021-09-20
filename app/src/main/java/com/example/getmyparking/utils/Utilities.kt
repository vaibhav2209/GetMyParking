package com.example.getmyparking.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.getmyparking.data.dto.ParkingErrorDto
import com.example.getmyparking.utils.Constants.GOOGLE_DRIVE_BASE_URL
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


object Utilities {

    fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }


    fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun clearEditTextFocus(mEditText: EditText, activity: Activity){
        mEditText.clearFocus()
        mEditText.text.clear()
        hideKeyBoard(activity)
    }

    private fun hideKeyBoard(activity: Activity){
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view: View? = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun openingTimeFormatter(starTime:String, endTime:String) {
        val date = SimpleDateFormat("hh-mm a", Locale.getDefault())
        val weekDay = SimpleDateFormat("EEE", Locale.getDefault())

        try {
            Timber.tag("DateFormatter").d("startTime: $starTime")
            val dateobj = date.parse(starTime)
            Timber.tag("DateFormatter").d("dateObj: $dateobj")
            val dat = date.format(dateobj)
            Timber.tag("DateFormatter").d("dat: $dat")
        }catch (e:Exception){
            Timber.tag("DateFormatter").d("exception: ${e.message}")
        }

        /*return StringBuilder()
            .append(date.format(date.parse(starTime)))
            .append("-")
            .append(date.format(date.parse(endTime)))
            .toString()*/
    }

    fun driveImageLinkGenerator(link:String): String {
        return if (link.contains("drive.google.com/")){
            StringBuilder()
                .append(GOOGLE_DRIVE_BASE_URL)
                .append(link.split("/").dropLast(1).last())
                .toString()
        } else
            link
    }

    fun deserializeParkingError(errorJson: String): ParkingErrorDto? {
        val type = object : TypeToken<ParkingErrorDto>() {}.type
        return Gson().fromJson(errorJson, type)
    }
}