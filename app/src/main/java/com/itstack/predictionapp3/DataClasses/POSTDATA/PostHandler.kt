package com.itstack.predictionapp3.DataClasses.POSTDATA

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.itstack.predictionapp3.RetrofitData.ApiService
import com.itstack.predictionapp3.RetrofitData.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostHandler(private val arr: Array<Int>, private val context: Context, private val onResponse: (String) -> Unit) {
    fun generateResponse() {
        val apiService = RetrofitInstance.getInstance().create(ApiService::class.java)

        // Create a PredictionRequest object with the array
        val predictionRequest = PredictionRequest(arr.toList())  // Convert array to list
        val call = apiService.predictResult(predictionRequest)  // Correct method name

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val prediction = response.body()?.data?.prediction ?: "No prediction available"
                    onResponse(prediction) // Pass prediction to the callback
                } else {
                    Toast.makeText(
                        context,
                        "Data not fetched: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("PostHandler", "Error: ${t.message}")
                Toast.makeText(context, "Request failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}


