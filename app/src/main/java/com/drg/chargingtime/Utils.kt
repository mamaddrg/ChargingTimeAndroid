package com.drg.chargingtime

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.widget.Toast
import java.util.*


class Utils {
    companion object {

        const val SEND_TIMER_BC_ACTION = "ACTION_GET_TOTAL_CHARGING_TIME"
        const val CHARGING_TIME_IN_SECONDS = "ChargingTimeSeconds"
        const val NOTIFICATION_CHANNEL_ID = "ChannelChargingTime"
        const val DATABASE_NAME = "dbCharge"


        fun createNotificationChannel(context : Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val serviceChannel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Charging Time Channel",
                    NotificationManager.IMPORTANCE_DEFAULT)
                val manager = context.getSystemService(NotificationManager::class.java)
                manager.createNotificationChannel(serviceChannel)
            }
        }


        fun makeOnChargeNotification(context : Context): Notification {

            val notificationIntent = Intent(context , MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)

            val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Charging Time")
                .setContentText("Total time of your device charge is computing.")
                .setSmallIcon(R.drawable.ic_status)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

            return notification
        }


        fun makeFullyChargedNotification(context: Context): Notification {

            val notificationIntent = Intent(context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)

            val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Full Charge")
                .setContentText("Device is fully charged. Please unplug charger to prevent battery harm")
                .setSmallIcon(R.drawable.ic_battery_full)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

            return notification
        }


        fun makeNotificationSound(context: Context) {

            try {
                val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val ring = RingtoneManager.getRingtone(context, notification)
                ring.play()
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }


        fun makeTimeFormatHMS(time: Int) : String {
            val hours = time / 3600
            val minutes = (time % 3600) / 60
            val seconds = time % 60
            return  String.format(Locale.ENGLISH, "%01d", hours) + ":" +
                    String.format(Locale.ENGLISH, "%02d", minutes) + ":" +
                    String.format(Locale.ENGLISH, "%02d", seconds)
        }


        fun getCurrentTimeHM(): String {
            val calendar = Calendar.getInstance(Locale.getDefault())
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            return "${String.format(Locale.ENGLISH, "%02d", hour)}:" +
                   "${String.format(Locale.ENGLISH, "%02d", minute)}"
        }


        fun getDateInSensibleWay(date : String) : String {

            // every date is saved in ****/**/** format
            // so we can parse year/month/day by the following method
            val dayCharge = date.substring(8,9).toInt()
            val monthCharge = date.substring(5,6).toInt()
            val yearCharge = date.substring(0,3).toInt()

            val currentDate = getCurrentDateYMD()
            val dayNow = currentDate.substring(8,9).toInt()
            val monthNow = currentDate.substring(5,6).toInt()
            val yearNow = currentDate.substring(0,3).toInt()

            if (yearCharge == yearNow && monthCharge == monthNow) {
                when (dayNow - dayCharge) {

                    0 -> return "today"
                    1 -> return "yesterday"
                    2 -> return "two days ago"
                    3 -> return "three days ago"
                    4 -> return "four days ago"
                    5 -> return "five days ago"
                    6 -> return "six days ago"
                    7 -> return "seven days ago"
                }
            }
            return date
        }


        fun getCurrentDateYMD() : String {
            val calender = Calendar.getInstance(Locale.getDefault())
            val year = calender.get(Calendar.YEAR)
            val month = calender.get(Calendar.MONTH) + 1
            val day = calender.get(Calendar.DAY_OF_MONTH)
            return "$year/" +
                   "${String.format(Locale.ENGLISH, "%02d", month)}/" +
                   "${String.format(Locale.ENGLISH, "%02d", day)}"
        }


        @SuppressLint("PrivateApi")
        fun getBatteryCapacity(context: Context): Int {

            val mPowerProfile: Any
            var batteryCapacity = 0.0
            val POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile"

            try {
                mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context::class.java)
                    .newInstance(context)

                batteryCapacity = Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getBatteryCapacity")
                    .invoke(mPowerProfile) as Double

            } catch (e: Exception) {
                Toast.makeText(context , "Can't get battery capacity :(" , Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
            return batteryCapacity.toInt()
        }

    }
}