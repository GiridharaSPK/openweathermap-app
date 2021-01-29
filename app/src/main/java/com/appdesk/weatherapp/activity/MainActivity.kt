package com.appdesk.weatherapp.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.appdesk.weatherapp.R
import com.appdesk.weatherapp.databinding.ActivityMainBinding
import com.appdesk.weatherapp.utils.ToastUtils.Companion.showToastLong

class MainActivity : AppCompatActivity() {
    private var activityMainBinding: ActivityMainBinding? = null
    private var handler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main);
        activityMainBinding = DataBindingUtil.setContentView(this@MainActivity,
            R.layout.activity_main
        )
        checkForegroundLocationPermissionAndRequest()
    }


    private fun checkForegroundLocationPermissionAndRequest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val hasLocationPermission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            if (!hasLocationPermission) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_FOREGROUND)
            } else {
                onForegroundPermissionGranted(true)
            }
        } else {
            onForegroundPermissionGranted(true)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_FOREGROUND) {
            if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onForegroundPermissionGranted(true)
            } else {
                onForegroundPermissionGranted(false)
            }
        }
    }

    private fun onForegroundPermissionGranted(isGrant: Boolean) {
        if (!isGrant) {
            showToastLong(this@MainActivity, "Location Permission is required")
            //or showSnackbar
            finish()
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            checkBackgroundLocationPermissionAndRequest()
        } else {
            handler = Handler()
            handler!!.postDelayed({
                startActivity(Intent(this@MainActivity, WeatherActivity::class.java))
                finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }, SPLASHTIME)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private fun checkBackgroundLocationPermissionAndRequest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val hasLocationPermission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
            if (!hasLocationPermission) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), REQUEST_CODE_FOREGROUND)
            } else {
                onBackgroundPermissionGranted(true)
            }
        } else {
            onBackgroundPermissionGranted(true)
        }
    }

    private fun onBackgroundPermissionGranted(isGrant: Boolean) {
        if (!isGrant) {
            showToastLong(this@MainActivity, "Permissions not granted")
            //or showSnackbar
            finish()
            return
        }
        handler = Handler()
        handler!!.postDelayed({
            startActivity(Intent(this@MainActivity, WeatherActivity::class.java))
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }, SPLASHTIME)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (handler != null) {
            handler!!.removeCallbacksAndMessages(null)
        }
    }

    companion object {
        private const val REQUEST_CODE_FOREGROUND = 100
        private const val SPLASHTIME: Long = 2000
    }
}