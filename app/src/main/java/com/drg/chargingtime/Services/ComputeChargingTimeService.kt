package com.drg.chargingtime.Services

import android.app.Service
import android.content.Intent
import android.os.IBinder

class ComputeChargingTimeService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        return START_STICKY
    }

}
