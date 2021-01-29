package com.appdesk.weatherapp.model

data class ForecastResponse(
    val lat: String,
    val lon: String,
    val timezone: String,
    val timezone_offset: String,
    val daily: List<Daily>
)