package com.appdesk.weatherapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SharedPreferenceUtil {

    companion object {

        fun init(context: Context) {
//            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val sharedPreferences by lazy {
                context.getSharedPreferences(
                    "main",
                    Context.MODE_PRIVATE
                )
            }
            editor = sharedPreferences.edit()
            Log.i("MyApplication", "SharePreferences init() $sharedPreferences")
        }

        var sharedPreferences: SharedPreferences? = null
        lateinit var editor: SharedPreferences.Editor


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

}