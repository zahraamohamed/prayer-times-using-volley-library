package com.example.myapplication.data.ui

import android.content.ContentValues
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.webkit.*
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.PrayerResponce
import com.example.myapplication.data.dataBase.PrayDataBase
import com.example.myapplication.data.dataBase.TableDetils
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {
    //make object from TaskDbHelper class
    lateinit var dataBaseHelper: PrayDataBase
    lateinit var cursor:Cursor
    private  lateinit var binding:ActivityMainBinding
    private val client= OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
         title="prayer times"

        /*
       *define the object of TaskDataBase
        */
        dataBaseHelper = PrayDataBase(this)
        // define cursor that used to read data from data base
        cursor=  dataBaseHelper.readableDatabase.rawQuery("Select * from ${TableDetils.TABLE_NAME}",
            arrayOf <String>())
        makeRequest("baghdad")
    }


    private fun makeRequest(country: String) {


        //build url by okhttp library
        val request= Request.Builder().url("https://api.pray.zone/v2/times/today.json?city=$country").build()

        //convert json file to string(parser json file) by using Gson library
        client.newCall(request).enqueue(object : Callback {


            override fun onFailure(call: Call, e: IOException) {
                e.message?.let { lazyLog(it) }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { jsonString ->

                    val result = Gson().fromJson(jsonString, PrayerResponce::class.java)


                    runOnUiThread {

                        var countryy = result.results.location.country
                        var city = result.results.location.city
                        var date = result.results.datetime.joinToString { it.date.gregorian }
                        var hijri = result.results.datetime.joinToString { it.date.hijri }
                        var fajr = result.results.datetime.joinToString { it.times.Fajr }
                        fajr.lazyLog()
                        var dhuhr = result.results.datetime.joinToString { it.times.Dhuhr }
                        var asr = result.results.datetime.joinToString { it.times.Asr }
                        var maghrib = result.results.datetime.joinToString { it.times.Maghrib }
                        var isha = result.results.datetime.joinToString { it.times.Isha }
                        var sunrise = result.results.datetime.joinToString { it.times.Sunrise }
                        var sunset = result.results.datetime.joinToString { it.times.Sunrise }





                        //read data

                        if (cursor.count != 0) {
                            readData()
                            cursor.close()


                        } else
                        {


                            // make entry to insert the value from view to database
                            val newEntry = ContentValues().apply {
                                put(TableDetils.DATE, date)
                                put(TableDetils.HIJRI, hijri)
                                put(TableDetils.CITY, city)
                                put(TableDetils.COUNTRY, countryy)
                                put(TableDetils.SUNRISE, sunrise)
                                put(TableDetils.SUNSET, sunset)
                                put(TableDetils.FAJR, fajr)
                                put(TableDetils.DHUHR, dhuhr)
                                put(TableDetils.ASR, asr)
                                put(TableDetils.ISHA, isha)
                                put(TableDetils.MAGHRIB, maghrib)



                            }
                            //set new entry in data base

                            writeInDatabase(newEntry, TableDetils.TABLE_NAME)
                         newEntry.lazyLog()
                            readData()


                        }
                    }


                }


            }


        })

    }


    /*
     this function to read data from sql light
     */

        fun readData() {

            //read data from database and store it in variable
                while (cursor.moveToNext()) {
                    val fajr = cursor.getString(0)
                    val dhuhr = cursor.getString(1)
                    val asr = cursor.getString(2)
                    val isha = cursor.getString(3)
                    val maghrib = cursor.getString(4)
                    val date = cursor.getString(5)
                    val city = cursor.getString(6)
                    val country = cursor.getString(7)
                    val hijri = cursor.getString(8)
                    val sunrise = cursor.getString(9)
                    val sunset = cursor.getString(10)

                    //get data to views

                    binding.date.text ="$date\n$hijri"
                    binding.city.text = city
                    binding.country.text = country
                    binding.fajer.text = fajr
                    binding.dhuhr.text = dhuhr
                    binding.asr.text = asr
                    binding.maghrib.text = maghrib
                    binding.isha.text = isha


                }
        }


    /*
    * this function to insert any new entry to any table exists in tasks database
    */
    private fun writeInDatabase(newEntry: ContentValues, table_name: String) {
        dataBaseHelper.writableDatabase.insert(
            table_name,
            null,
            newEntry
        )
    }




    fun <T> T.lazyLog(tag: String = "LAZY_LOG"): T {
        Log.i(tag, toString())
        return this
    }
}



