package com.appdesk.weatherapp.api

import com.appdesk.weatherapp.model.CityHistoryData
import com.appdesk.weatherapp.model.CurrentWeatherResponse
import com.appdesk.weatherapp.model.ForecastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") apiKey: String
    ): Response<CurrentWeatherResponse>

    @GET("onecall")
    suspend fun getForecastReport(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String,
        @Query("appid") apiKey: String
    ): Response<ForecastResponse>

    //history/city?id={id}&type=hour&start={start}&cnt={cnt}&appid={API key}
    @GET("history")
    fun getCityHistoryData(
        @Query("id") id: String,
        @Query("type") type: String,
        @Query("start") start: String,
        @Query("appid") apiKey: String

    ): Response<CityHistoryData>

}


