package com.appdesk.weatherapp.utils

class TempUtils {
    companion object {
        fun kelvinToCen(temp: Double): Double {
            return temp - 273.15
        }

        fun kelvinToFar(temp: Double): Double {
            return 1.8 * (temp - 273) + 32
        }

    }
}