package com.appdesk.weatherapp.fragment

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(TAG, "onAttach")
    }

    /*override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_current, container, false)
        val view: View = binding.root
        if (this.con == null) {
            this.con = activity
            this.act = activity as WeatherActivity?
        }
        Log.i(TAG, "onCreateView")
        return view
    }*/

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated")
        initView()
    }

    private fun initView() {
        /*if (context != null) {
            locationManager =
                context!!.getSystemService(
                    LOCATION_SERVICE::class.java
                ) as LocationManager?
//        request = WeatherRequest()
            if (locationManager != null)
                checkAndGetLocation()
            else
                Log.e(TAG, "location manager null")
        } else {
            Log.e(TAG, "context null")
        }*/
//        callWeatherDataApi(request)
//        setupSwipeToRefresh() //todo
    }

    /* private fun checkAndGetLocation() {
         if (!locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
             OnGPS()
         } else {
             getLocation()
         }
     }

     private fun getLocation() {
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
         val locationGPS: Location? =
             locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
         if (locationGPS != null) {
 //            val df = DecimalFormat("#.####")
 //            df.roundingMode = RoundingMode.CEILING
             *//*for (Number n : Arrays.asList(12, 123.12345, 0.23, 0.1, 2341234.212431324)) {
                Double d = n.doubleValue();
                System.out.println(df.format(d));
            }*//*
            lat = locationGPS.latitude
            lon = locationGPS.longitude
//            latitude = df.format(lat).toString()
//            longitude = df.format(longi).toString()
        }
    }

    private fun OnGPS() {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton(
            "Yes"
        ) { _, _ -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            .setNegativeButton(
                "No"
            ) { dialog, _ -> dialog.cancel() }
        val alertDialog = builder.create()
        alertDialog.show()
    }*/

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
                    Log.e(TAG, "response : ${response.body()}")
                    val main = response.body()!!.main
                    val temp = main.temp
                    val tempC = TempUtils.kelvinToCen(temp)
                    val tempF = TempUtils.kelvinToFar(temp)
                    Log.i(TAG, "C: $tempC \nF : $tempF")
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

    private fun showLoading() {
        //todo
    }

    private fun hideLoading() {
        //todo
    }


}