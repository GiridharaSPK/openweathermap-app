package com.appdesk.weatherapp.model

data class CityHistoryData(
    val calctime: Double,
    val city_id: Int,
    val cnt: Int,
    val cod: String,
    val list: List<>,
    val message: String
)