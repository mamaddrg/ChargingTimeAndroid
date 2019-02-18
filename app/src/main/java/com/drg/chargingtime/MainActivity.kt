package com.drg.chargingtime

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.drg.chargingtime.Fragments.HistoryFragment
import com.drg.chargingtime.Fragments.InfoFragment
import com.drg.chargingtime.Fragments.StatusFragment
import com.drg.chargingtime.Services.WaitingForPowerService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_status.*
import me.zhanghai.android.materialprogressbar.MaterialProgressBar

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!WaitingForPowerService.isPowerListenerRunning)
            startService(Intent(this , WaitingForPowerService::class.java))

        val status = StatusFragment()
        val info = InfoFragment()
        val history = HistoryFragment()
        var activeFragment : Fragment = status
        val manager = supportFragmentManager
        manager.beginTransaction().add(R.id.fl_main, history , "3").hide(history).commit()
        manager.beginTransaction().add(R.id.fl_main, info , "2").hide(info).commit()
        manager.beginTransaction().add(R.id.fl_main , status , "1").commit()

        bnv_main.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {

                R.id.menu_status -> {
                    manager.beginTransaction().hide(activeFragment).show(status).commit()
                    activeFragment = status
                }

                R.id.menu_info -> {
                    manager.beginTransaction().hide(activeFragment).show(info).commit()
                    activeFragment = info
                }

                R.id.menu_history -> {
                    manager.beginTransaction().hide(activeFragment).show(history).commit()
                    history.updateList()
                    activeFragment = history
                }
            }

            bnv_main.menu.findItem(item.itemId).isChecked = true
            return@setOnNavigationItemSelectedListener false
        }

    }
}
