package com.example.myapplication.data.dataBase

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class PrayDataBase (context: Context) : SQLiteOpenHelper(context, DBNAME, null, DBVERSION) {

    /*
     *on create database
    */
    override fun onCreate(dataBase: SQLiteDatabase?) {
        val sql = "CREATE TABLE ${TableDetils.TABLE_NAME}(" +
                "${TableDetils.FAJR} TEXT PRIMARY KEY," +
                "${TableDetils.DHUHR} TEXT," +
                "${TableDetils.ASR} TEXT," +
                "${TableDetils.ISHA} TEXT," +
                "${TableDetils.MAGHRIB} TEXT," +
                "${TableDetils.DATE} TEXT," +
                "${TableDetils.CITY} TEXT," +
                "${TableDetils.COUNTRY} TEXT," +
                "${TableDetils.HIJRI} TEXT," +
                "${TableDetils.SUNSET} TEXT," +
                "${TableDetils.SUNRISE} TEXT" +
                ")"
        dataBase?.execSQL(sql)
    }

    /*
      *on update version
    */
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }

    companion object {
        private const val DBNAME = "TaskDbHelper"
        private const val DBVERSION = 1
    }



}