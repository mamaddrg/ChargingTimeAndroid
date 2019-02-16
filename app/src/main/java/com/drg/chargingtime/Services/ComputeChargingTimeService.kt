package com.drg.chargingtime.Services

import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.IBinder
import com.drg.chargingtime.Database.ChargingDB
import com.drg.chargingtime.Model.ChargeObject
import com.drg.chargingtime.Utils
import java.util.*

class ComputeChargingTimeService : Service() {

    private var timeOnCharge = 0
    private var batteryLevel = 0
    private var timerTask = MainTimer()
    private val batteryChangedBC = BatteryChangedBroadcast()
    companion object {
        var isServiceRunning = false
        var primitiveChargePercent = -1 // if it's -1 , means that it has not been set yet and service is not running
        var startChargingTime = "00:00"
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    override fun onCreate() {
        super.onCreate()
        Utils.createNotificationChannel(this)
        startForeground(1, Utils.makeOnChargeNotification(this))
        timerTask = MainTimer()
        Timer().scheduleAtFixedRate(timerTask , 1000 , 1000)
        timerTask.run()
        registerReceiver(batteryChangedBC , IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        isServiceRunning = true
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(batteryChangedBC)
        isServiceRunning = false
        primitiveChargePercent = -1
        timerTask.cancel()
    }



    private inner class BatteryChangedBroadcast : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {

            val batteryStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)

            if (batteryLevel == 100) {
                Utils.createNotificationChannel(this@ComputeChargingTimeService)
                Utils.makeNotificationSound(this@ComputeChargingTimeService)
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(2, Utils.makeFullyChargedNotification(this@ComputeChargingTimeService))
                saveChargeDataInDB()
                isServiceRunning = false
                stopSelf()
            }
            else if (batteryStatus == BatteryManager.BATTERY_STATUS_DISCHARGING ||
                     batteryStatus == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
                saveChargeDataInDB()
                stopSelf()
            }

            if (primitiveChargePercent == -1) { // save start point charge percent just once for the first time
                primitiveChargePercent = batteryLevel
                startChargingTime = Utils.getCurrentTimeHM()
            }
        }
    }


    private fun saveChargeDataInDB() {

        val charge = ChargeObject(
            0 ,
            Utils.getCurrentDateYMD(),
            Utils.getCurrentTimeHM(),
            (timeOnCharge / 60),
            primitiveChargePercent,
            batteryLevel
        )
        ChargingDB(this).insertChargeItem(charge)
    }


    private inner class MainTimer : TimerTask() {

        private val intentBC = Intent(Utils.SEND_TIMER_BC_ACTION)

        override fun run() {
            timeOnCharge++
            intentBC.putExtra(Utils.CHARGING_TIME_IN_SECONDS, timeOnCharge)
            sendBroadcast(intentBC)
        }
    }

}
