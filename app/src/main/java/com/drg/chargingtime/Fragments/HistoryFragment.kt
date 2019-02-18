package com.drg.chargingtime.Fragments

import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.drg.chargingtime.ActionHistoryListClick
import com.drg.chargingtime.Adapters.AdapterHistoryList
import com.drg.chargingtime.Database.ChargingDB
import com.drg.chargingtime.Dialogs.DialogAddDescription
import com.drg.chargingtime.Model.ChargeObject
import com.drg.chargingtime.R

class HistoryFragment : Fragment() {

    private var adapter : AdapterHistoryList? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_history , container , false)
        val recyclerHistory = view.findViewById<RecyclerView>(R.id.recycler_history)
        adapter = AdapterHistoryList(context!! , makeListener() , ChargingDB(context!!).getAllItems())
        recyclerHistory.layoutManager = LinearLayoutManager(context , LinearLayoutManager.VERTICAL , false)
        recyclerHistory.adapter = adapter

        return view
    }


    fun updateList() {

        adapter?.updateHistoryList(ChargingDB(context!!).getAllItems())
    }


    private fun makeListener(): AdapterHistoryList.OnHistoryItemClicked {
        return object : AdapterHistoryList.OnHistoryItemClicked{
            override fun historyItemClicked(position: Int , chargeObject: ChargeObject , action : ActionHistoryListClick) {

                when (action) {
                    ActionHistoryListClick.Delete -> deleteItemRequest(chargeObject)
                    ActionHistoryListClick.AddDescription -> addDescriptionRequest(chargeObject)
                }
            }
        }
    }


    private fun deleteItemRequest(chargeObject: ChargeObject) {

        val dialogBuilder = AlertDialog.Builder(context!!)
        dialogBuilder.setTitle("Warning")
        dialogBuilder.setMessage("Are you sure?\nItem will be removed.")
        dialogBuilder.setPositiveButton("Delete" , DialogInterface.OnClickListener { _, _ ->

            if (ChargingDB(context!!).deleteItem(chargeObject))
                Toast.makeText(context, "Deleted successfully", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(context, "Error while deleting item!", Toast.LENGTH_LONG).show()

            updateList()
        })
        dialogBuilder.setNegativeButton("Cancel" , DialogInterface.OnClickListener { dialog, _ -> dialog.cancel() })
        dialogBuilder.show()

    }


    private fun addDescriptionRequest(chargeObject: ChargeObject) {

        val dialog = DialogAddDescription()
        dialog.loadClickListener(object : DialogAddDescription.OnAddDescriptionClicked{
            override fun onAddDescriptionClicked(description: String) {

                chargeObject.description = description
                if (ChargingDB(context!!).editItem(chargeObject))
                    Toast.makeText(context , "Description added successfully" , Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(context , "Failed to add description" , Toast.LENGTH_LONG).show()

                updateList()
            }
        })
        dialog.show(activity?.supportFragmentManager , "AddDescription")
    }

}