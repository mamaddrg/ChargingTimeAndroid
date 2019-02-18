package com.drg.chargingtime.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import com.drg.chargingtime.ActionHistoryListClick
import com.drg.chargingtime.Model.ChargeObject
import com.drg.chargingtime.R
import com.drg.chargingtime.Utils


class AdapterHistoryList (private val context : Context,
                          private val historyItemClickeListener : OnHistoryItemClicked,
                          private var list : ArrayList<ChargeObject>)
    : RecyclerView.Adapter<AdapterHistoryList.HistoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): HistoryViewHolder {

        val view = HistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_history_list , parent , false))
        view.itemView.setOnClickListener {
            val popupMenu = PopupMenu(context , view.itemView)
            popupMenu.inflate(R.menu.history_list_click_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {

                    R.id.history_list_menu_description ->
                        historyItemClickeListener.historyItemClicked(view.position , list[list.size - view.position - 1] , ActionHistoryListClick.AddDescription)
                    R.id.history_list_menu_delete ->
                        historyItemClickeListener.historyItemClicked(view.position , list[list.size - view.position - 1] , ActionHistoryListClick.Delete)
                }
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        }

        return view
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
                    "Charging time : ${if(charge.onChargeTime == 0) "less than a minute" else "${charge.onChargeTime} minutes"}" +
                    "${if (charge.description.length > 0) "\nDescription : ${charge.description}" else ""}"
    }


    inner class HistoryViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvHistoryList = itemView.findViewById<TextView>(R.id.tv_item_history)!!
    }


    fun updateHistoryList(list : ArrayList<ChargeObject>) {

        this.list = list
        notifyDataSetChanged()
    }


    interface OnHistoryItemClicked {
        fun historyItemClicked(position: Int ,chargeObject: ChargeObject ,action: ActionHistoryListClick){}
    }

}