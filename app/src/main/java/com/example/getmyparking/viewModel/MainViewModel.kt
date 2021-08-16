package com.example.getmyparking.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Response
import com.example.getmyparking.models.Parking
import com.example.getmyparking.models.ParkingLot
import com.example.getmyparking.network.GsonRequest
import com.example.getmyparking.network.RequestType
import com.example.getmyparking.network.WebServiceProvider
import com.example.getmyparking.utils.Resource
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class MainViewModel @Inject constructor(
    val mainRepository: MainRepository,
    val webServiceProvider: WebServiceProvider
) : ViewModel(){

    val parkingData: MutableLiveData<Resource<Parking>> = MutableLiveData()
    val parkingLots: MutableLiveData<Resource<List<ParkingLot>>> = MutableLiveData()

    open fun getParkingData(parking: Parking ,url:String, header: HashMap<String?, String?>) = viewModelScope.launch {
        parkingData.postValue(Resource.Loading())

        val gsonRequest:GsonRequest<Parking> = GsonRequest.getGsonRequest(RequestType.GET,
            url = url,
            requestBody = GsonBuilder().create().toJson(parking),
            clazz = Parking::class.java,
            header = header,
            listener = { response->
                parkingData.postValue(Resource.Success(data = response))
                parkingLots.postValue(Resource.Success(data = response.parkingLots))
            },
            { error->
                parkingData.postValue(error.message?.let { Resource.Error(message = it) })
            })

        webServiceProvider.addToRequestQueue(gsonRequest)
    }

}