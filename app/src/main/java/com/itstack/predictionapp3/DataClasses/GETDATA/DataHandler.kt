package com.itstack.predictionapp3.DataClasses.GETDATA

import android.content.Context
import com.itstack.predictionapp3.RetrofitData.ApiService
import com.itstack.predictionapp3.RetrofitData.RetrofitInstance

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object DataHandler {

    fun fetchRegisterData(context: Context, onDataFetched: (MainData?) -> Unit) {
        val retrofit = RetrofitInstance.getInstance()
        val apiService = retrofit.create(ApiService::class.java)

        apiService.getRegisterData().enqueue(object : Callback<RegisterData> {
            override fun onResponse(call: Call<RegisterData>, response: Response<RegisterData>) {
                if (response.isSuccessful) {
                    val registerData = response.body()
                    // Accessing the main_data from RegisterData
                    onDataFetched(registerData?.data?.main_data)
                } else {
                    onDataFetched(null)
                }
            }

            override fun onFailure(call: Call<RegisterData>, t: Throwable) {
                onDataFetched(null)
            }
        })
    }
}
