package com.appdesk.weatherapp.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.appdesk.weatherapp.R
import com.appdesk.weatherapp.api.ApiClient
import com.appdesk.weatherapp.databinding.ActivityWeatherBinding
import com.appdesk.weatherapp.fragment.CurrentFragment
import com.appdesk.weatherapp.fragment.DateFragment
import com.appdesk.weatherapp.fragment.ReportsFragment
import com.appdesk.weatherapp.fragment.SettingsFragment
import com.appdesk.weatherapp.model.WeatherRequest
import com.appdesk.weatherapp.utils.ToastUtils
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.*

const val TAG = "WeatherActivity"

class WeatherActivity : AppCompatActivity() {
//    private var response: ApiResponse?

    private lateinit var settingsFragment: SettingsFragment
    private lateinit var dateFragment: DateFragment
    private lateinit var currentFragment: CurrentFragment
    private lateinit var reportsFragment: ReportsFragment
    private lateinit var binding: ActivityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_weather)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setListeners()


    }

    private fun initView() {
        binding.pbLoading.isVisible = false
        binding.bottomAppBar.bottomNavigationMenu.background = null

        currentFragment = CurrentFragment()
        dateFragment = DateFragment()
        reportsFragment = ReportsFragment()
        settingsFragment = SettingsFragment()

        setCurrentFragment(currentFragment)

    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_container, fragment)
            commit()
        }
    }

    private fun setListeners() {
        binding.etCity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s?.isNotBlank() == true)
                    getWeatherDetails(s.toString())
            }
        })

        binding.bottomAppBar.bottomNavigationMenu.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.it_current -> {
                    binding.etCity.visibility = View.VISIBLE
                    setCurrentFragment(currentFragment)
                }
                R.id.it_report -> {
                    binding.etCity.visibility = View.VISIBLE
                    setCurrentFragment(reportsFragment)
                }
                R.id.it_date -> {
                    binding.etCity.visibility = View.VISIBLE
                    setCurrentFragment(dateFragment)
                }
                R.id.it_settings -> {
                    binding.etCity.visibility = View.GONE
                    setCurrentFragment(settingsFragment)
                }
            }
            true//handled selection listener
        }
    }

    fun getWeatherDetails(cityName: String) {
        binding.pbLoading.isVisible = true
        lifecycleScope.launch {
            val response = try {
                ApiClient.getWeather(WeatherRequest(cityName))
            } catch (e: IOException) {
                ToastUtils.showToastShort(
                    context = this@WeatherActivity,
                    "Please check your network"
                )
                Log.e(TAG, "Caught : ", e)
                return@launch
            } catch (e: HttpException) {
                ToastUtils.showToastShort(
                    context = this@WeatherActivity,
                    "Please check your network"
                )
                Log.e(TAG, "Caught : ", e)
                return@launch
            } catch (e: Exception) {
                Log.i(TAG, "===========6")
                ToastUtils.showToastLong(context = this@WeatherActivity, e.toString())
                Log.e(TAG, "Caught : ", e)
                return@launch
            }
            if (response != null) {
                if (response.isSuccessful && response.body() != null) {
//                    binding.textView.text = response.body().toString()
                    Log.e(TAG, "response : ${response.body()}")
                    val main = response.body()!!.main
                    val temp = main.temp
                    val temp_c = toCen(temp)
                    val temp_f = toFar(temp)
                    Log.i(TAG, "C: $temp_c \nF : $temp_f")
                } else {
                    binding.etCity.error = "Please enter valid city"
                }
            } else {
                Log.e(TAG, "Null response")
                ToastUtils.showToastShort(context = this@WeatherActivity, "Response null")
            }
            binding.pbLoading.isVisible = false
        }
    }

    private fun toCen(temp: Double): Double {
        return temp - 273.15
    }

    private fun toFar(temp: Double): Double {
        return 1.8 * (temp - 273) + 32
    }

}