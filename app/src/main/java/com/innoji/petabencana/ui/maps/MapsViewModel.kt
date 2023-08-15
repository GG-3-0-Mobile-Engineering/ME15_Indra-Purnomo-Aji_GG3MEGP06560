package com.innoji.petabencana.ui.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.innoji.petabencana.data.network.api.ApiConfig
//import com.innoji.petabencana.di.DisasterRepository
import com.innoji.petabencana.data.network.response.GeometriesItem
import com.innoji.petabencana.data.network.response.ReportResponse
//import com.innoji.petabencana.di.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MapsViewModel : ViewModel() {


    private val _reportData = MutableLiveData<List<GeometriesItem>>()
    val reportData: LiveData<List<GeometriesItem>> = _reportData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _returnResponse = MutableLiveData<Int>()
    val returnResponse: LiveData<Int> = _returnResponse

    fun searchReport(admin: String? = null, disaster: String? = null){
        _isLoading.value = true
        val service = ApiConfig.getApiService().getReports(604800, admin, disaster)
        service.enqueue(object: Callback<ReportResponse> {
            override fun onResponse(
                call: Call<ReportResponse>,
                response: Response<ReportResponse>
            ) {
                _isLoading.value = false
                if(response.code() == 400){
                    _returnResponse.value = response.code()
                }else{
                    if(response.isSuccessful){
                        val responseBody = response.body()
                        if(responseBody != null){
                            _returnResponse.value = response.code()
                            _reportData.value = response.body()?.result?.objects?.output?.geometries
                        }
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