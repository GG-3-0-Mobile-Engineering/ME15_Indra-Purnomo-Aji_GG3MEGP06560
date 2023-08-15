package com.innoji.petabencana.di

import com.innoji.petabencana.data.network.api.ApiService
import com.innoji.petabencana.data.network.response.GeometriesItem

//class ReportRepositoryImpl(private val apiService: ApiService) : ReportRepository {
//    override suspend fun getReports(admin: String?, disaster: String?): List<GeometriesItem> {
//        val response = apiService.getReports(604800, admin, disaster)
//        if (response.isSuccessful) {
//            return response.body()?.result?.objects?.output?.geometries ?: emptyList()
//        } else {
//            throw Exception("Failed to fetch data")
//        }
//    }
//}
