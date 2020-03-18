package com.sumit.hungergo.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.provider.Settings
import androidx.core.app.ActivityCompat

class ConnectionManager {

    fun checkConnection(context: Context):Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork : NetworkInfo? = connectivityManager.activeNetworkInfo
        if(activeNetwork?.isConnected != null){
            return activeNetwork.isConnected
        }else{
            return false;
        }
    }

}