package com.drg.chargingtime.Fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.drg.chargingtime.R
import com.drg.chargingtime.Utils

class InfoFragment : Fragment() {

    private lateinit var tvInfo : TextView
    private val batteryInfoReceiver = GetBatteryInfoReceiver()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_info , container , false)
        tvInfo = view.findViewById(R.id.tv_info)
        context?.registerReceiver(batteryInfoReceiver , IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        return view
    }


    override fun onDestroy() {
        super.onDestroy()
        context?.unregisterReceiver(batteryInfoReceiver)
    }


    inner class GetBatteryInfoReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            val voltage = intent?.getIntExtra(BatteryManager.EXTRA_VOLTAGE , -1)
            val temp = intent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE , -1)
            val healthKey = intent?.getIntExtra(BatteryManager.EXTRA_HEALTH , -1)
            val technology = intent?.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)
            val capacity = Utils.getBatteryCapacity(context!!)
            val temperature : Double = temp!!.toDouble() / 10

            val health : String = when (healthKey) {
                BatteryManager.BATTERY_HEALTH_COLD -> "Cold"
                BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
                BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
                BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Over heat"
                BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Over voltage"
                BatteryManager.BATTERY_HEALTH_UNKNOWN -> "Unknown"
                BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "Unspecified failure"
                else -> "Can't get health data"
            }
            tvInfo.text = "Battery specs : \n" +
                          "Capacity : $capacity mAh \n" +
                          "Voltage : $voltage mV \n" +
                          "Temperature : $temperature C \n" +
                          "Technology : $technology \n" +
                          "Health state : $health"
        }
    }

}