package com.example.getmyparking.viewModel

import androidx.lifecycle.viewModelScope
import com.android.volley.Response
import com.example.getmyparking.models.Parking
import com.example.getmyparking.network.GsonRequest
import com.example.getmyparking.network.RequestType
import com.example.getmyparking.network.WebServiceProvider
import com.example.getmyparking.utils.Resource
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainRepository @Inject constructor(
    val webServiceProvider: WebServiceProvider
) {

    /*fun getParkingData(parking: Parking ,url:String, header: HashMap<String?, String?>):GsonRequest<T>  {
         Resource.Loading()

        val gsonRequest:GsonRequest<Parking> = GsonRequest.getGsonRequest(RequestType.GET,
            url = url,
            requestBody = GsonBuilder().create().toJson(parking),
            clazz = Parking::class.java,
            header = header,
            listener = { response->
                return@getGsonRequest Resource.Success(data = response))
            },
            { error->
                parkingData.postValue(error.message?.let { Resource.Error(message = it) })
            })

        webServiceProvider.addToRequestQueue(gsonRequest)

        return gsonRequest
    }*/

}