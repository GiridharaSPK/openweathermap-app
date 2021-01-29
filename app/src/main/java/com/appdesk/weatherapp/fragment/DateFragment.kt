package com.appdesk.weatherapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.appdesk.weatherapp.R
import com.appdesk.weatherapp.activity.WeatherActivity
import com.appdesk.weatherapp.api.ApiClient
import com.appdesk.weatherapp.model.CityHistoryRequest
import com.appdesk.weatherapp.utils.ToastUtils
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class DateFragment(activity: WeatherActivity) : Fragment(R.layout.fragment_date) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {

//        getCityHistoryData()
    }

    fun getCityHistoryData(cityId: String, start: String, type: String) {
        Log.i(TAG, "getCurrentWeatherDetails")
        showLoading()
        lifecycleScope.launch {
            val response = try {
                ApiClient.getCityHistoryData(
                    CityHistoryRequest(
                        cityId = cityId,
                        type = type,
                        start = start
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
                    Log.e(TAG, "cityHistoryResponse : ${response.body()}")
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
