package com.itstack.predictionapp3.DataClasses.GETDATA

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.itstack.predictionapp3.RetrofitData.ApiService
import com.itstack.predictionapp3.RetrofitData.RetrofitInstance

class RegisterDataHandler(private val context: Context) {

    fun registretionData(onResult: (Data) -> Unit, onError: (String) -> Unit) {
        val apiService = RetrofitInstance.getInstance().create(ApiService::class.java)
        val call = apiService.getRegisterData()

        call.enqueue(object : Callback<RegisterData> {
            override fun onResponse(call: Call<RegisterData>, response: Response<RegisterData>) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.data?.let {
                        onResult(it)
                    } ?: onError("Data not found")
                } else {
                    onError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterData>, t: Throwable) {
                onError("Error: ${t.message}")
            }
        })
    }
}
