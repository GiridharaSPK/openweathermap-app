package com.appdesk.weatherapp.activity

import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.appdesk.weatherapp.R
import com.appdesk.weatherapp.databinding.ActivityWeatherBinding
import com.appdesk.weatherapp.fragment.CurrentFragment
import com.appdesk.weatherapp.fragment.DateFragment
import com.appdesk.weatherapp.fragment.ReportsFragment
import com.appdesk.weatherapp.fragment.SettingsFragment
import com.appdesk.weatherapp.utils.ToastUtils


class WeatherActivity : AppCompatActivity() {
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

        /* locationManager = getSystemService(
             LOCATION_SERVICE
         ) as LocationManager?
         if (locationManager != null)
             checkAndGetLocation()
         else
             Log.e(TAG, "location manager null")*/

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

    /*   private fun checkAndGetLocation() {
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
               locationManager!!.requestLocationUpdates(
                   LocationManager.GPS_PROVIDER,
                   5000,
                   0F
               ) {
                   currentFragment.getCurrentWeatherDetails(it.latitude, it.longitude)
               }
           }
       }


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
       }*/

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