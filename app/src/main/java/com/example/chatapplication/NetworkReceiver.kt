package com.example.chatapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast

class NetworkReceiver: BroadcastReceiver() {

    companion object{
        var isNetworkConnected = false
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val manager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = manager.activeNetwork
            val networkCapabilities = manager.getNetworkCapabilities(activeNetwork)

            if(networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
                isNetworkConnected = true
            else
                isNetworkConnected = false


        }


    }


}