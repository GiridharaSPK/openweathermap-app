package com.appdesk.weatherapp.fragment

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.appdesk.weatherapp.R
import com.appdesk.weatherapp.activity.WeatherActivity
import com.appdesk.weatherapp.api.ApiClient
import com.appdesk.weatherapp.databinding.FragmentCurrentBinding
import com.appdesk.weatherapp.model.CurrentWeatherRequest
import com.appdesk.weatherapp.utils.TempUtils
import com.appdesk.weatherapp.utils.ToastUtils
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


open class CurrentFragment(weatherActivity: WeatherActivity) :
    Fragment(R.layout.fragment_current) {

    private lateinit var binding: FragmentCurrentBinding
    val TAG = "CurrentFragment"
    private var lon: Double = 0.0
    private var act: WeatherActivity? = null
    private var lat: Double = 0.0
    private var locationManager: LocationManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_current, container, false)
        val view = binding.root
//        if (context == null) {
//            context = activity
//            activity = activity as DashboardActivity?
//        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated")
        initView()
    }

    private fun initView() {
        if (context != null) {
            locationManager =
                context!!.getSystemService(
                    LOCATION_SERVICE
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
            Log.e(TAG, "Location Permission not granted ")
            return
        }
//        var locationGPS: Location? = null
        if (locationManager != null) {
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,
                0F
            ) {
                getCurrentWeatherDetails(it.latitude, it.longitude)
            }
        } else {
            Log.e(TAG, "location manager null")
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

    fun getCurrentWeatherDetails(lat: Double, lon: Double) {
        Log.i(TAG, "getCurrentWeatherDetails")
        showLoading()
        lifecycleScope.launch {
            val response = try {
                ApiClient.getCurrentWeather(
                    CurrentWeatherRequest(
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
                    Log.e(TAG, "currentWeatherResponse : ${response.body()}")
                    val main = response.body()!!.main
                    val temp = main.temp
                    val tempC = TempUtils.kelvinToCen(temp!!)
                    val tempF = TempUtils.kelvinToFar(temp)
                    Log.i(TAG, "C: $tempC \nF : $tempF")
                } else {
//                    binding.etCity.error = "Please enter valid city"
                }
            } else {
                Log.e(TAG, "Null response")
                if (activity != null)
                    ToastUtils.showToastShort(context = activity, "Response null")
            }
            hideLoading()
        }
    }

    private fun showLoading() {
        //todo
    }

    private fun hideLoading() {
        //todo
    }


}