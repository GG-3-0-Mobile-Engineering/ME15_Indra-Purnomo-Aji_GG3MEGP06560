package com.innoji.petabencana.data.network.api

import com.innoji.petabencana.data.network.response.ReportResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("reports")
    fun getReports(
        @Query("timeperiod") timePeriod: Long = 604800,
        @Query("admin") admin: String? = null,
        @Query("disaster") disaster: String? = null
    ): Call<ReportResponse>
}