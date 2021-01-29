package com.appdesk.weatherapp.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.appdesk.weatherapp.activity.WeatherActivity
import com.appdesk.weatherapp.api.ApiClient
import com.appdesk.weatherapp.model.ForecastRequest
import com.appdesk.weatherapp.utils.ToastUtils
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ReportsFragment(activity: WeatherActivity) : Fragment() {
    val TAG = "CurrentFragment"
    private var lon: Double = 0.0
    private var act: WeatherActivity? = null
    private var lat: Double = 0.0
    private var locationManager: LocationManager? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        if (context != null) {
            locationManager =
                context!!.getSystemService(
                    Context.LOCATION_SERVICE
                ) as LocationManager?
            if (locationManager != null)
                checkAndGetLocation()
            else
                Log.e(TAG, "location manager null")
        } else {
            Log.e(TAG, "context null")
        }
    }


    private fun checkAndGetLocation() {
        if (!locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS()
        } else {
            getLocation()
        }
    }

    private fun getLocation() {
        Log.i(TAG, "getLocation")
        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        var locationGPS: Location? = null
        if (locationManager != null) {
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,
                0F
            ) {
                getWeatherForecast(it.latitude, it.longitude)
            }
        }
    }

    private fun OnGPS() {
        val builder = AlertDialog.Builder(activity!!)
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton(
            "Yes"
        ) { _, _ -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            .setNegativeButton(
                "No"
            ) { dialog, _ -> dialog.cancel() }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun getWeatherForecast(lat: Double, lon: Double) {
        Log.i(TAG, "getWeatherForecast")
        showLoading()
        lifecycleScope.launch {
            val response = try {
                ApiClient.getForecast(
                    ForecastRequest(
                        lat = lat.toString(),
                        lon = lon.toString()
                    )
                )
            } catch (e: IOException) {
                ToastUtils.showToastShort(
                    context = activity,
                    "Please check your network"
                )
                Log.e(TAG, "Caught : ", e)
                return@launch
            } catch (e: HttpException) {
                ToastUtils.showToastShort(
                    context = activity,
                    "Please check your network"
                )
                Log.e(TAG, "Caught : ", e)
                return@launch
            } catch (e: Exception) {
                ToastUtils.showToastLong(context = activity, e.toString())
                Log.e(TAG, "Caught : ", e)
                return@launch
            }
            if (response != null) {
                if (response.isSuccessful && response.body() != null) {
//                    binding.textView.text = response.body().toString()
                    Log.e(TAG, "WeatherForecastResponse : ${response.body()}")
                } else {
//                    binding.etCity.error = "Please enter valid city"
                }
            } else {
                Log.e(TAG, "Null response")
                ToastUtils.showToastShort(context = activity, "Response null")
            }
            hideLoading()
        }
    }

    private fun hideLoading() {

    }

    private fun showLoading() {


    }

}
