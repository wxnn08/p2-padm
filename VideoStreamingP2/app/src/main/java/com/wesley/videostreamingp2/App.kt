package com.wesley.videostreamingp2

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class App : Application() {

    companion object {
        lateinit var appContext: Context
            private set

        fun unregisterBroadcast(receiver: BroadcastReceiver) {
            LocalBroadcastManager.getInstance(appContext).unregisterReceiver(receiver)
        }

        fun registerBroadcast(receiver: BroadcastReceiver, filter: IntentFilter) {
            LocalBroadcastManager.getInstance(appContext).registerReceiver(receiver, filter)
        }

        fun isNetworkConnected(): Boolean {
            val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo

            if(networkInfo != null && networkInfo.isConnected)
                return true
            return false
        }

        fun sendBroadcast(intent: Intent) {
            LocalBroadcastManager.getInstance(appContext).sendBroadcast(intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
}