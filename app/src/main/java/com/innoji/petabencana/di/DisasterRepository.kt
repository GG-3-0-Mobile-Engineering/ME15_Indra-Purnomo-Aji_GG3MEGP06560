package com.innoji.petabencana.di

import android.util.Log
import com.innoji.petabencana.data.network.api.ApiService
import com.innoji.petabencana.data.network.response.GeometriesItem
import com.innoji.petabencana.data.network.response.ReportResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

//@Singleton
//class DisasterRepository @Inject constructor(private val apiService: ApiService){
//
//    fun searchReport(admin: String?, disaster: String?): List<GeometriesItem>{
//        val response: Response<ReportResponse> = apiService.getReports(604800, admin, disaster)
//        Log.d("API_RESPONSE", response.toString())
//        if (response.isSuccessful && response.body() != null) {
//            return response.body()?.result?.objects?.output?.geometries ?: emptyList()
//        }
//        throw Exception("Failed to fetch data from API")
//    }
//}