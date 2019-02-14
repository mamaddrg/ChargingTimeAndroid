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
import com.drg.chargingtime.Database.ChargingDB
import com.drg.chargingtime.R
import com.drg.chargingtime.Services.ComputeChargingTimeService
import com.drg.chargingtime.Utils
import me.zhanghai.android.materialprogressbar.MaterialProgressBar

class StatusFragment : Fragment() {

    private lateinit var tvBatteryLevel: TextView
    private lateinit var tvChargingTime: TextView
    private lateinit var tvChargingInfo: TextView
    private lateinit var pbBatteryLevel: MaterialProgressBar
    private val batteryChangedBroadcast = OnBatteryChangedBroadcast()
    private val chargeTimeBroadcast = OnChargeTimeBroadcast()
    private var batteryPercentage = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_status , container , false)
        pbBatteryLevel = view.findViewById(R.id.pb_show_battery_level)
        tvChargingTime = view.findViewById(R.id.tv_show_charging_time)
        tvChargingInfo = view.findViewById(R.id.tv_extra_info)
        tvBatteryLevel = view.findViewById(R.id.tv_show_battery_level)

        context?.registerReceiver(batteryChangedBroadcast , IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        context?.registerReceiver(chargeTimeBroadcast , IntentFilter(Utils.SEND_TIMER_BC_ACTION))

        if (ComputeChargingTimeService.isServiceRunning) {
            tvChargingInfo.text =
                        "Start charging in ${ComputeChargingTimeService.startChargingTime} \n" +
                        "Start Point :" + " ${ComputeChargingTimeService.primitiveChargePercent}%"
            tvChargingTime.text = "Total Charging Time \n Loading..."
        }
        else {
            val chargeObject = ChargingDB(context!!).getLastItem()
            if (chargeObject == null) {
                tvChargingInfo.text =
                            "There is no saved data.\n" +
                            "It seems to be first time that app is running.\n" +
                            "we hope you enjoy :)"
            }
            else {
                tvChargingInfo.text =
                            "Last charge was ${Utils.getDateInSensibleWay(chargeObject.date)} \n" +
                            "Finish point in ${chargeObject.time} \n" +
                            "Start in ${chargeObject.primitivePercentage}% " +
                            "to ${chargeObject.finalPercentage}% \n" +
                            "Charged ${chargeObject.finalPercentage - chargeObject.primitivePercentage}% " +
                            "in ${if(chargeObject.onChargeTime == 0) "less than a minute" else "${chargeObject.onChargeTime} minutes"}"
            }
        }
        return view
    }


    override fun onDestroy() {
        super.onDestroy()
        context?.unregisterReceiver(batteryChangedBroadcast)
        context?.unregisterReceiver(chargeTimeBroadcast)
    }


    private inner class OnBatteryChangedBroadcast : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent) {

            val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            batteryPercentage = intent.getIntExtra(BatteryManager.EXTRA_LEVEL , 0)
            tvBatteryLevel.text = "$batteryPercentage%"
            pbBatteryLevel.progress = batteryPercentage
        }
    }


    private inner class OnChargeTimeBroadcast : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {

            val timeInSeconds = intent.getIntExtra(Utils.CHARGING_TIME_IN_SECONDS , 0)
            tvChargingTime.text = "Total Charging Time \n ${Utils.makeTimeFormatHMS(timeInSeconds)}"
        }

    }
}