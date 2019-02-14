package com.drg.chargingtime

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.drg.chargingtime.Services.WaitingForPowerService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startService(Intent(this , WaitingForPowerService::class.java))
    }
}
