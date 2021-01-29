package com.appdesk.weatherapp.utils

import android.content.Context
import android.widget.Toast

class ToastUtils {

    companion object {
        /**
         * @param context
         * @param message
         * @return
         */
        fun showToastShort(context: Context?, message: String) {
            if (!message.isEmpty()) Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        /**
         * @param context
         * @param message
         * @return
         */
        fun showToastLong(context: Context?, message: String) {
            if (!message.isEmpty()) Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    init {
        throw Error("U will not able to instantiate it")
    }
}