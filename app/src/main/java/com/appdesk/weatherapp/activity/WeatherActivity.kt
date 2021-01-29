package com.appdesk.weatherapp.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.appdesk.weatherapp.api.ApiClient
import com.appdesk.weatherapp.databinding.ActivityWeatherBinding
import com.appdesk.weatherapp.model.WeatherRequest
import com.appdesk.weatherapp.utils.ToastUtils
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class WeatherActivity : AppCompatActivity() {
//    private var response: ApiResponse?

    private lateinit var binding: ActivityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_weather)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()

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
                return@launch
            } catch (e: HttpException) {
                ToastUtils.showToastShort(
                    context = this@WeatherActivity,
                    "Please check your network"
                )
                return@launch
            } catch (e: Exception) {
                ToastUtils.showToastLong(context = this@WeatherActivity, e.toString())
                return@launch
            }
            binding.pbLoading.isVisible = false
            if (response != null) {
                if (response.isSuccessful && response.body() != null) {
//                    binding.textView.text = response.body().toString()
                    val main = response.body()!!.main
                    val temp = main.temp
                    val temp_c = toCen(temp)
                    val temp_f = toFar(temp)

                } else {
                    binding.etCity.setError("Please enter valid city")
                }
            }
        }
    }

    private fun toCen(temp: Double): Double {
        return temp - 273.15
    }

    private fun toFar(temp: Double): Double {
        return 1.8 * (temp - 273) + 32
    }

}