package com.sillylife.dd.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager


class ConnectivityReceiver() : BroadcastReceiver() {
    private var connectivityReceiverListener: ConnectivityReceiverListener? = null

    constructor(connectivityReceiverListener: ConnectivityReceiverListener? = null) : this() {
        this.connectivityReceiverListener = connectivityReceiverListener
    }

    override fun onReceive(context: Context?, p1: Intent?) {
        val connectivityService = context?.getSystemService(Context.CONNECTIVITY_SERVICE)
        if (connectivityService != null && connectivityService is ConnectivityManager) {
            val cm = connectivityService as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            val isConnected =
                    activeNetwork != null && activeNetwork.isAvailable && activeNetwork.isConnectedOrConnecting
            if (connectivityReceiverListener != null) {
                connectivityReceiverListener?.onNetworkConnectionChanged(isConnected)
            }
        }
    }

    companion object {
        public fun isConnected(context: Context?): Boolean {
            val cm = context?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isAvailable && activeNetwork.isConnectedOrConnecting
        }
    }


}