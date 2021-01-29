package com.appdesk.weatherapp.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.appdesk.weatherapp.R
import com.appdesk.weatherapp.databinding.ActivityMainBinding
import com.appdesk.weatherapp.enums.PreferenceValue
import com.appdesk.weatherapp.utils.SharedPreferenceUtil
import com.appdesk.weatherapp.utils.ToastUtils
import com.appdesk.weatherapp.utils.ToastUtils.Companion.showToastLong


class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"

    private var activityMainBinding: ActivityMainBinding? = null
    private var handler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main);
        activityMainBinding = DataBindingUtil.setContentView(
            this@MainActivity,
            R.layout.activity_main
        )
        initView()
        setListener()
        checkForegroundLocationPermissionAndRequest()
    }

    private fun initView() {
        activityMainBinding!!.btEnter.isVisible = false
        activityMainBinding!!.etUsername.isVisible = false
        activityMainBinding!!.tvWelcome.isVisible = true
    }

    private fun checkForegroundLocationPermissionAndRequest() {
        Log.d(TAG, "checkForegroundLocationPermissionAndRequest")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val hasLocationPermission = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            if (!hasLocationPermission) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    REQUEST_CODE_FOREGROUND
                )
            } else {
                onForegroundPermissionGranted(true)
            }
        } else {
            onForegroundPermissionGranted(true)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_FOREGROUND) {
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                onForegroundPermissionGranted(true)
            } else {
                onForegroundPermissionGranted(false)
            }
        }
    }

    private fun onForegroundPermissionGranted(isGrant: Boolean) {
        Log.d(TAG, "onForegroundPermissionGranted")
        if (!isGrant) {
            showToastLong(this@MainActivity, "Location Permission is required")
            //or showSnackbar
            finish()
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            checkBackgroundLocationPermissionAndRequest()
        } else {
            showUsernameInput()
        }
    }

    private fun showUsernameInput() {
        Log.d(TAG, "showUsernameInput")
        activityMainBinding!!.etUsername.isVisible = true
        activityMainBinding!!.tvWelcome.isVisible = false
        activityMainBinding!!.btEnter.isVisible = false
    }

    private fun setListener() {
        activityMainBinding!!.etUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s?.isNotBlank() == true) {
                    activityMainBinding!!.btEnter.isVisible = true
                }
            }
        })
        activityMainBinding!!.btEnter.setOnClickListener {
            if (activityMainBinding!!.etUsername.text.isNotBlank()) {
                openWeatherActivity()
                saveUserName()
            } else {
                ToastUtils.showToastShort(context = this@MainActivity, "Enter username")
            }
        }
    }

    private fun saveUserName() {
        Log.d(TAG, "saveUserName")
        SharedPreferenceUtil.editor.putString(
            PreferenceValue.USERNAME.name,
            activityMainBinding!!.etUsername.text.toString()
        )
        SharedPreferenceUtil.editor.commit()
    }

    private fun openWeatherActivity() {
        Log.d(TAG, "openWeatherActivity")
        handler = Handler()
        handler!!.postDelayed({
            startActivity(Intent(this@MainActivity, WeatherActivity::class.java))
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }, SPLASHTIME)
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private fun checkBackgroundLocationPermissionAndRequest() {
        Log.d(TAG, "checkBackgroundLocationPermissionAndRequest")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val hasLocationPermission = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            if (!hasLocationPermission) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    REQUEST_CODE_FOREGROUND
                )
            } else {
                onBackgroundPermissionGranted(true)
            }
        } else {
            onBackgroundPermissionGranted(true)
        }
    }

    private fun onBackgroundPermissionGranted(isGrant: Boolean) {
        Log.d(TAG, "onBackgroundPermissionGranted")
        if (!isGrant) {
            showToastLong(this@MainActivity, "Permissions not granted")
            //or showSnackbar
            finish()
            return
        }
        showUsernameInput()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (handler != null) {
            handler!!.removeCallbacksAndMessages(null)
        }
    }

    companion object {
        private const val REQUEST_CODE_FOREGROUND = 100
        private const val SPLASHTIME: Long = 500
    }
}