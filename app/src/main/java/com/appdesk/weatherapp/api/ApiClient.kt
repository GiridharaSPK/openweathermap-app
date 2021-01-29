package com.appdesk.weatherapp.api

import android.util.Log
import com.appdesk.weatherapp.enums.PreferenceValue
import com.appdesk.weatherapp.model.ApiResponse
import com.appdesk.weatherapp.model.WeatherRequest
import com.appdesk.weatherapp.utils.SharedPreferenceUtil.Companion.sharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val TAG = "ApiClient"

object ApiClient {

    private var apiInterface: ApiInterface

    init {
//        this.context = context;
        val baseUrl = "https://api.openweathermap.org/data/2.5/"
        val clientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        clientBuilder.apply {
            connectTimeout(2, TimeUnit.SECONDS)
            readTimeout(2, TimeUnit.SECONDS)
            addInterceptor { chain ->
                val original: Request = chain.request()
                val request: Request = original.newBuilder()
                    .addHeader("Content-Encoding", "UTF-8")
                    .method(original.method(), original.body())
                    .header("Accept-Encoding", "identity")
                    .build()
                chain.proceed(request)
            }
        }
        val gson: Gson = GsonBuilder()
            .setLenient()
            .disableHtmlEscaping()
            .create()
        val client = clientBuilder.build();
        apiInterface = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiInterface::class.java);
    }
/*
    @Synchronized
    public fun getInstance(): ApiClient {
        if (apiClient == null) {
            apiClient = ApiClient()
        }
        return apiClient!!
    }*/


    suspend fun getWeather(weatherRequest: WeatherRequest): Response<ApiResponse>? {
        Log.i(TAG, "getWeatherRequest : $weatherRequest")
        return sharedPreferences?.getString(PreferenceValue.API_KEY.name, "")?.let {
            apiInterface.getWeather(
                cityName = weatherRequest.cityName,
                apiKey = it
            )
        }
    }


}