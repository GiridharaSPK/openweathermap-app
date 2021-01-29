package com.appdesk.weatherapp.application

import android.app.Application
import android.content.Context
import android.util.Log
import com.appdesk.weatherapp.enums.PreferenceValue
import com.appdesk.weatherapp.utils.SharedPreferenceUtil
import com.appdesk.weatherapp.utils.ToastUtils


class MyApplication : Application() {
    //    private val isTabletDevice = false
    val appContext: Context?
        get() = mMyApplication

    override fun onLowMemory() {
        super.onLowMemory()
//        LogUtils.showErrorLog(TAG, "onLowMemory()")
        ToastUtils.showToastShort(appContext, "Memory low")
    }

    /*override fun onTerminate() {
        super.onTerminate()
//        LogUtils.showErrorLog(TAG, "onTerminate()")
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
//        LogUtils.showInfoLog(TAG, "onTrimMemory($level)")
    }*/

    override fun onCreate() {
        super.onCreate()
//        LogUtils.showInfoLog(TAG, "<<<onCreate()")
        mMyApplication = this
        Log.i(TAG, "onCreate")
        SharedPreferenceUtil.init(this)
        SharedPreferenceUtil.editor.putString(PreferenceValue.API_KEY.name, "")
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e(
                TAG,
                "Application Restart ",
                throwable
            )
        }
    }

    companion object {
        private val TAG = MyApplication::class.java.simpleName
        private var mMyApplication: MyApplication? = null
    }
}