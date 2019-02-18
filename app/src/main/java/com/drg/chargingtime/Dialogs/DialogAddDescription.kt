package com.drg.chargingtime.Dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.drg.chargingtime.R

class DialogAddDescription : DialogFragment() {

    private var onClicked: OnAddDescriptionClicked? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.dialog_add_description , container , false)
        val btnAdd = view.findViewById<Button>(R.id.btn_add_description_dialog)
        val etAdd = view.findViewById<EditText>(R.id.et_add_description_dialog)
        btnAdd.setOnClickListener {
            onClicked?.onAddDescriptionClicked(etAdd.text.toString())
            dismiss()
        }
        return view
    }


    fun loadClickListener(onClicked: OnAddDescriptionClicked) {
        this.onClicked = onClicked
    }

    interface OnAddDescriptionClicked{
        fun onAddDescriptionClicked(description : String)
    }

}