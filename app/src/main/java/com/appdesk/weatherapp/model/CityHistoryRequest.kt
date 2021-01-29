package com.appdesk.weatherapp.model

data class CityHistoryRequest(
    val cityId: String,
    val type: String,
    val start: String
)

