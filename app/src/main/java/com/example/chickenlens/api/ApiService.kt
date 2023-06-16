package com.example.chickenlens.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("last-6-day")
    fun getLastSixDay(): Call<List<dayAvgResponse>>

    @GET("data")
    fun getAllDay(): Call<List<dayAvgResponse>>

    //    Tampilkan pada grafik saja
    @GET("perminggu")
    fun getAllWeek(): Call<List<weekAvgResponse>>

    //    Tampilkan pada detail
    @GET("data-pilih")
    fun getDetailDay(
        @Query("hari") day: Int,
    ): Call<dayAvgResponse>

    @POST("panen")
    fun harvest() : Call<panenResponse>


}