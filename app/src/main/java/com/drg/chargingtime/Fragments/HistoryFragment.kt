package com.drg.chargingtime.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.drg.chargingtime.Adapters.AdapterHistoryList
import com.drg.chargingtime.Database.ChargingDB
import com.drg.chargingtime.R

class HistoryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_history , container , false)
        val recyclerHistory = view.findViewById<RecyclerView>(R.id.recycler_history)
        val adapter = AdapterHistoryList(context!! , ChargingDB(context!!).getAllItems())
        recyclerHistory.layoutManager = LinearLayoutManager(context , LinearLayoutManager.VERTICAL , false)
        recyclerHistory.adapter = adapter

        return view
    }

}