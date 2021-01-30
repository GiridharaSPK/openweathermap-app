package com.appdesk.weatherapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.appdesk.weatherapp.R
import com.appdesk.weatherapp.activity.WeatherActivity
import com.appdesk.weatherapp.databinding.FragmentSettingsBinding
import com.appdesk.weatherapp.enums.PreferenceValue
import com.appdesk.weatherapp.utils.SharedPreferenceUtil


class SettingsFragment(activity: WeatherActivity) : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    val TAG = "SettingsFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        //        if (context == null) {
//            context = activity
//            activity = activity as DashboardActivity?
//        }
        return binding.root
    }


    private fun initView() {
        Log.i(TAG, "initView")
        if (SharedPreferenceUtil.sharedPreferences != null)
            binding.tvUsername.text =
                SharedPreferenceUtil.sharedPreferences!!.getString(
                    PreferenceValue.USERNAME.name,
                    ""
                ) else {
            Log.e(TAG, "shared preferences are null")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setListener()
    }

    private fun setListener() {
        Log.i(TAG, "setListener")
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

