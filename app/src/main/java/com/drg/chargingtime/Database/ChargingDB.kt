package com.drg.chargingtime.Database

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.drg.chargingtime.Model.ChargeObject
import com.drg.chargingtime.Utils

class ChargingDB(context: Context) : SQLiteOpenHelper(context , Utils.DATABASE_NAME , null , 1) {


    override fun onCreate(db: SQLiteDatabase?) {
        try {
            db?.execSQL("CREATE TABLE IF NOT EXISTS tbl_history(id INTEGER PRIMARY KEY AUTOINCREMENT , date DATE , time TEXT , on_charge_time INTEGER , primitive_percent INTEGER , final_percent INTEGER , description TEXT);")
        } catch (e: SQLException) { }
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}


    fun insertChargeItem(charge: ChargeObject) : Boolean {

        val cv = ContentValues()
        cv.put("date", charge.date)
        cv.put("time", charge.time)
        cv.put("on_charge_time", charge.onChargeTime)
        cv.put("primitive_percent", charge.primitivePercentage)
        cv.put("final_percent", charge.finalPercentage)
        cv.put("description" , charge.description)

        val db = writableDatabase
        val isOk = db.insert("tbl_history", null, cv)
        return isOk > 0
    }


    fun getAllItems() : ArrayList<ChargeObject> {

        val list = ArrayList<ChargeObject>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tbl_history", null)

        if (cursor.count > 0) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                list.add(ChargeObject(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getInt(5),
                    cursor.getString(6)))
                cursor.moveToNext()
            }
        }
        cursor.close()
        return list
    }


    fun getLastItem() : ChargeObject? {

        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tbl_history", null)

        cursor.moveToLast()
        if (cursor.count > 0) {
            val chargeObject = ChargeObject(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getInt(3),
                cursor.getInt(4),
                cursor.getInt(5),
                cursor.getString(6)
            )
            cursor.close()
            return chargeObject
        }

        return null
    }


    fun deleteItem(charge: ChargeObject) : Boolean {

        return writableDatabase.delete("tbl_history" , "id=${charge.id}" , null) > 0
    }


    fun editItem(charge: ChargeObject) : Boolean {

        val cv = ContentValues()
        cv.put("id" , charge.id)
        cv.put("time", charge.time)
        cv.put("on_charge_time", charge.onChargeTime)
        cv.put("primitive_percent", charge.primitivePercentage)
        cv.put("final_percent", charge.finalPercentage)
        cv.put("description" , charge.description)

        val db = writableDatabase
        val isOk = db.update("tbl_history" , cv , "id=${charge.id}" ,null)
        return isOk > 0
    }

}