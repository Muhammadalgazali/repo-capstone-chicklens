package com.example.chickenlens.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chickenlens.api.ApiConfig
import com.example.chickenlens.api.dayAvgResponse
import com.example.chickenlens.api.panenResponse
import com.example.chickenlens.api.weekAvgResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _chickens = MutableLiveData<List<dayAvgResponse>>()
    val chickens: LiveData<List<dayAvgResponse>> = _chickens

    private val _grafik = MutableLiveData<List<weekAvgResponse>>()
    val grafik: LiveData<List<weekAvgResponse>> = _grafik

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _harvest = MutableLiveData<panenResponse>()
    val harvest: LiveData<panenResponse> = _harvest



    companion object {
        private const val TAG = "MainViewModel"
    }

    init {
        getAllWeek()
    }

    fun harvest(){
        _isLoading.value = true
        val client = ApiConfig.getApiService().harvest()
        client.enqueue(object : Callback<panenResponse>{
            override fun onResponse(call: Call<panenResponse>, response: Response<panenResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _harvest.value = response.body()
                    Log.d(TAG, "${response.message()}")
                }else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<panenResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "Kesalahan : ${t.message.toString()}")
            }

        })
    }

    fun getAllWeek(){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllWeek()
        client.enqueue(object : Callback<List<weekAvgResponse>>{
            override fun onResponse(
                call: Call<List<weekAvgResponse>>,
                response: Response<List<weekAvgResponse>>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _grafik.value = response.body()
                    Log.d(TAG, "${response.message()}")
                }else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<weekAvgResponse>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "Kesalahan : ${t.message.toString()}")
            }

        })
    }


    fun getLastSixDay() {
        _isLoading.value =true
        val client = ApiConfig.getApiService().getLastSixDay()
        client.enqueue(object : Callback<List<dayAvgResponse>>{
            override fun onResponse(
                call: Call<List<dayAvgResponse>>,
                response: Response<List<dayAvgResponse>>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _chickens.value = response.body()
                    Log.d(TAG, "${response.message()}")
                }else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<dayAvgResponse>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "Kesalahan : ${t.message.toString()}")
            }

        })
    }

    fun getAllDay() {
        _isLoading.value =true
        val client = ApiConfig.getApiService().getAllDay()
        client.enqueue(object : Callback<List<dayAvgResponse>>{
            override fun onResponse(
                call: Call<List<dayAvgResponse>>,
                response: Response<List<dayAvgResponse>>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _chickens.value = response.body()
                    Log.d(TAG, "${response.message()}")
                }else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<dayAvgResponse>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "Kesalahan : ${t.message.toString()}")
            }

        })
    }


}