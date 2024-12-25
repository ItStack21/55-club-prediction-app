package com.itstack.predictionapp3.RetrofitData


import com.itstack.predictionapp3.DataClasses.GETDATA.RegisterData
import com.itstack.predictionapp3.DataClasses.POSTDATA.ApiResponse
import com.itstack.predictionapp3.DataClasses.POSTDATA.PredictionRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST



// In your API interface
interface ApiService {
    @POST("predict-result")
    fun predictResult(@Body request: PredictionRequest): Call<ApiResponse>

    @GET("get-home-data-app-three") // Correct endpoint
    fun getRegisterData(): Call<RegisterData>
}