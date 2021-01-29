package com.appdesk.weatherapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.appdesk.weatherapp.R
import com.appdesk.weatherapp.activity.WeatherActivity

class ReportsFragment(activity: WeatherActivity) : Fragment() {
    /*companion object {
        fun newInstance(): ReportsFragment {
            return ReportsFragment(this)
        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reports, container, false)
    }

}
