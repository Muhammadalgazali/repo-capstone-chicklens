package com.example.chickenlens.view.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chickenlens.api.ApiConfig
import com.example.chickenlens.api.dayAvgResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel :ViewModel() {

    private val _chicken = MutableLiveData<dayAvgResponse>()
    val chicken: LiveData<dayAvgResponse> = _chicken

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "DetailViewModel"
    }

    fun getDetailDay(day : Int){
        _isLoading.value =true
        val client = ApiConfig.getApiService().getDetailDay(day)
        client.enqueue(object : Callback<dayAvgResponse>{
            override fun onResponse(
                call: Call<dayAvgResponse>,
                response: Response<dayAvgResponse>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _chicken.value = response.body()
                    Log.d(TAG, "${response.message()}")
                }else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<dayAvgResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "Kesalahan : ${t.message.toString()}")
            }

        })
    }

}