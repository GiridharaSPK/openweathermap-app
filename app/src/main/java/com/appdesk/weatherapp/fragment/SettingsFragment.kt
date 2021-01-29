package com.appdesk.weatherapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.appdesk.weatherapp.R
import com.appdesk.weatherapp.activity.WeatherActivity
import com.appdesk.weatherapp.databinding.FragmentSettingsBinding
import com.appdesk.weatherapp.enums.PreferenceValue
import com.appdesk.weatherapp.utils.SharedPreferenceUtil

const val TAG = "SettingsFragment"

class SettingsFragment(activity: WeatherActivity) : Fragment(R.layout.fragment_settings) {

    private lateinit var binding: FragmentSettingsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        if (SharedPreferenceUtil.sharedPreferences != null)
            binding.tvUsername.text =
                SharedPreferenceUtil.sharedPreferences!!.getString(
                    PreferenceValue.USERNAME.name,
                    ""
                ) else {
            Log.e(TAG, "shared preferences are null")
        }
    }

    private fun setListener() {
        binding.rgTemp.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_c -> {
                    SharedPreferenceUtil.editor.putBoolean(PreferenceValue.CELSIUS.name, true)
//todo                    showSnackbar()
                }
                R.id.rb_f -> {
                    SharedPreferenceUtil.editor.putBoolean(PreferenceValue.CELSIUS.name, false)
//todo                    showSnackbar()
                }
            }
            SharedPreferenceUtil.editor.commit()
        }
    }


}

