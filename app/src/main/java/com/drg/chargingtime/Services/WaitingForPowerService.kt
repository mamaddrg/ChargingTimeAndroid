package com.drg.chargingtime.Services

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.IBinder
import android.widget.Toast

class WaitingForPowerService : Service() {

    private val powerConnectedBroadcast = PowerConnectedBroadcast()
    companion object { var isPowerListenerRunning = false }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    override fun onCreate() {
        super.onCreate()
        registerReceiver(powerConnectedBroadcast , IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        isPowerListenerRunning = true
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }


    inner class PowerConnectedBroadcast : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            if (intent?.getIntExtra(BatteryManager.EXTRA_STATUS , -1) == BatteryManager.BATTERY_STATUS_CHARGING) {
                if (!ComputeChargingTimeService.isServiceRunning){
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
                        startService(Intent(context, ComputeChargingTimeService::class.java))
                    else
                        startForegroundService(Intent(context, ComputeChargingTimeService::class.java))
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(powerConnectedBroadcast)
        isPowerListenerRunning = false
    }
}
