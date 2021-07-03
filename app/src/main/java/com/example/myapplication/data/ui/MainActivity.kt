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
import com.android.volley.Request
import com.android.volley.Response

import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.data.PrayerResponce
import com.example.myapplication.data.dataBase.PrayDataBase
import com.example.myapplication.data.dataBase.TableDetils
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONException
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {
    //make object from TaskDbHelper class
    private var requestQueue: RequestQueue? = null
    private  lateinit var binding:ActivityMainBinding
    private val client= OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
         title="prayer times"
        requestQueue = Volley.newRequestQueue(this)

        makeRequest("paris")
    }


    private fun makeRequest(country: String) {

        val url = "https://api.pray.zone/v2/times/today.json?city=$country"
        val request = JsonObjectRequest(Request.Method.GET, url, null, {
                response ->try {

            val jsonArray = response.getJSONArray("datetime")
            for (i in 0 until jsonArray.length()) {
                val employee = jsonArray.getJSONObject(i)
                val firstName = employee.getString("Sunrise")
                val age = employee.getInt("Fajr")
                firstName.lazyLog()
                binding.country.text=firstName
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        }, { error -> error.printStackTrace() })
        requestQueue?.add(request)
    }











    fun <T> T.lazyLog(tag: String = "LAZY_LOG"): T {
        Log.i(tag, toString())
        return this
    }
}



