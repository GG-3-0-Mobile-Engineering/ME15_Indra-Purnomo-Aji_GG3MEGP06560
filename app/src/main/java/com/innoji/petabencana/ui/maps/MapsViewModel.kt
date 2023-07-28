package com.innoji.petabencana.ui.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.innoji.petabencana.data.network.api.ApiConfig
import com.innoji.petabencana.data.network.response.GeometriesItem
import com.innoji.petabencana.data.network.response.Properties
import com.innoji.petabencana.data.network.response.ReportResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel: ViewModel() {

    private val _reportData = MutableLiveData<List<GeometriesItem>>()
    val reportData: LiveData<List<GeometriesItem>> = _reportData

    private val _coordinateData = MutableLiveData<List<GeometriesItem>>()
    val coordinateData: LiveData<List<GeometriesItem>> = _coordinateData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun searchReport(admin: String? = null, disaster: String? = null){
        _isLoading.value = true
        val service = ApiConfig.getApiService().getReports(604800, admin, disaster)
        service.enqueue(object: Callback<ReportResponse>{
            override fun onResponse(
                call: Call<ReportResponse>,
                response: Response<ReportResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null){
                        _reportData.value = response.body()?.result?.objects?.output?.geometries

                    }
                }
            }

            override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("mapsViewModel", "onFailure: ${t.message.toString()}" )
            }

        })

    }

}