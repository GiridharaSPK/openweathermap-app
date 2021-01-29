package com.appdesk.weatherapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SharedPreferenceUtil(context: Context) {
    companion object {
        public var sharedPreferences: SharedPreferences? = null
        lateinit var editor: SharedPreferences.Editor
    }

    init {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        editor = sharedPreferences.edit()
    }


    fun putValue(key: String?, value: String?) {
        editor.putString(key, value)
    }

    fun putValue(key: String?, value: Boolean) {
        editor.putBoolean(key, value)
    }

    fun save() {
        editor.commit()
    }

    fun clear() {
        editor.clear()
    }

    fun getString(key: String?, defValue: String?): String? {
        return sharedPreferences!!.getString(key, defValue)
    }

    fun getBoolean(key: String?, defValue: Boolean): Boolean {
        return sharedPreferences!!.getBoolean(key, defValue)
    }

}