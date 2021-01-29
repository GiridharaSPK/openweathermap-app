package com.appdesk.weatherapp.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.appdesk.weatherapp.R
import com.appdesk.weatherapp.databinding.ActivityWeatherBinding
import com.appdesk.weatherapp.fragment.CurrentFragment
import com.appdesk.weatherapp.fragment.DateFragment
import com.appdesk.weatherapp.fragment.ReportsFragment
import com.appdesk.weatherapp.fragment.SettingsFragment
import com.appdesk.weatherapp.utils.ToastUtils


class WeatherActivity : AppCompatActivity(), LocationListener {
    //    private var response: ApiResponse?
    val TAG = "WeatherActivity"
    private var locationManager: LocationManager? = null
    private var doublePress: Boolean = false
    private lateinit var settingsFragment: SettingsFragment
    private lateinit var dateFragment: DateFragment
    private lateinit var currentFragment: CurrentFragment
    private lateinit var reportsFragment: ReportsFragment
    private lateinit var binding: ActivityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
//        setContentView(R.layout.activity_weather)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setListeners()

    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart")
        currentFragment = CurrentFragment(this)
        dateFragment = DateFragment(this)
        reportsFragment = ReportsFragment(this)
        settingsFragment = SettingsFragment(this)

        setCurrentFragment(currentFragment)

        locationManager = getSystemService(
            LOCATION_SERVICE
        ) as LocationManager?
        if (locationManager != null)
            checkAndGetLocation()
        else
            Log.e(TAG, "location manager null")

    }

    private fun initView() {
        binding.pbLoading.isVisible = false
        binding.bottomAppBar.bottomNavigationMenu.background = null
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_container, fragment)
            commit()
        }
    }

    private fun setListeners() {
        /*  binding.etCity.addTextChangedListener(object : TextWatcher {
              override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

              override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

              override fun afterTextChanged(s: Editable?) {
                  if (s?.isNotBlank() == true)
                      getCurrentWeatherDetails(s.toString())
              }
          })*/

        binding.bottomAppBar.bottomNavigationMenu.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.it_current -> {
                    setCurrentFragment(currentFragment)
                }
                R.id.it_report -> {
                    setCurrentFragment(reportsFragment)
                }
                R.id.it_date -> {
                    setCurrentFragment(dateFragment)
                }
                R.id.it_settings -> {
                    setCurrentFragment(settingsFragment)
                }
            }
            true//handled selection listener
        }
    }

    fun showLoading() {
        binding.pbLoading.isVisible = true
    }

    fun hideLoading() {
        binding.pbLoading.isVisible = false
    }

    private fun checkAndGetLocation() {
        Log.i(TAG, "checkAndGetLocation")
        if (!locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS()
        } else {
            getLocation()
        }
    }

    override fun onLocationChanged(location: Location) {
        Log.d(TAG, "location : $location")
    }

    private fun getLocation() {
        Log.i(TAG, "getLocation")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        var locationGPS: Location? = null
        if (locationManager != null) {
            locationGPS = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//            locationGPS = getLastKnownLocation(locationManager!!)
            if (locationGPS != null) {
//            val df = DecimalFormat("#.####")
//            df.roundingMode = RoundingMode.CEILING
                /*for (Number n : Arrays.asList(12, 123.12345, 0.23, 0.1, 2341234.212431324)) {
                    Double d = n.doubleValue();
                    System.out.println(df.format(d));
                }*/
                val lat = locationGPS.latitude
                val lon = locationGPS.longitude
                currentFragment.getCurrentWeatherDetails(lat, lon)
//            latitude = df.format(lat).toString()
//            longitude = df.format(longi).toString()
            } else {
                Log.e(TAG, "locationGps null")
            }
        } else {
            Log.e(TAG, "location manager null")
        }
    }

//    private fun getLastKnownLocation(locationManager: LocationManager): Location? {
////        locationManager = applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
//        val providers: List<String> = locationManager.getProviders(true)
//        var bestLocation: Location? = null
//        for (provider in providers) {
//            val l: Location = locationManager.getLastKnownLocation(provider) ?: continue
//            if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
//                // Found best last known location: %s", l);
//                bestLocation = l
//            }
//        }
//        return bestLocation
//    }

    private fun OnGPS() {
        Log.i(TAG, "onGPS")
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton(
            "Yes"
        ) { _, _ -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            .setNegativeButton(
                "No"
            ) { dialog, _ -> dialog.cancel() }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onBackPressed() {
        if (doublePress) {
            super.onBackPressed()
            return
        }
        ToastUtils.showToastShort(this, "Press back again to exit app")
        Handler().postDelayed({
            doublePress = false
        }, 2000)
    }

}