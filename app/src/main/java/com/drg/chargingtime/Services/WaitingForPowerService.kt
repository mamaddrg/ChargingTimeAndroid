package com.drg.chargingtime.Services

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.widget.Toast

class WaitingForPowerService : Service() {


    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        registerReceiver(PowerConnectedBroadcast() , IntentFilter(Intent.ACTION_POWER_CONNECTED))
        //registerReceiver(PowerDisConnectedBroadcast() , IntentFilter(Intent.ACTION_POWER_DISCONNECTED))
        return START_STICKY
    }



    inner class PowerConnectedBroadcast : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

        }
    }


//    inner class PowerDisConnectedBroadcast : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//
//
//
//        }
//    }


}
