package com.appdesk.weatherapp.model

data class HistoryWeather(
    val main: Main?,
    val wind: Wind?,
    val clouds: Clouds?,
    val weather: List<Weather>?,
    val rain: Rain?,
    val dt: String?
)
