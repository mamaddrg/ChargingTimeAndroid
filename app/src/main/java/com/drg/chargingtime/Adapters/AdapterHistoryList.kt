package com.drg.chargingtime.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.drg.chargingtime.Model.ChargeObject
import com.drg.chargingtime.R
import com.drg.chargingtime.Utils

class AdapterHistoryList (private val context : Context,
                          private var list : ArrayList<ChargeObject>)
    : RecyclerView.Adapter<AdapterHistoryList.HistoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): HistoryViewHolder {
        return HistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_history_list , parent , false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {

        val charge = list[list.size - position-1] // From Last Item To First
        holder.tvHistoryList.text =
            "Date : ${Utils.getDateInSensibleWay(charge.date)} \n" +
                    "Time : ${charge.time} \n" +
                    "Start from : ${charge.primitivePercentage}% \n" +
                    "Finish in : ${charge.finalPercentage}% \n" +
                    "Charging time : ${if(charge.onChargeTime == 0) "less than a minute" else "${charge.onChargeTime} minutes"} \n"
    }


    inner class HistoryViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvHistoryList = itemView.findViewById<TextView>(R.id.tv_item_history)!!
    }

}