package com.example.myapplication.data.ui

import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.gson.Gson
import org.json.JSONException
import java.util.*


class MainActivity : AppCompatActivity() {
    //make object from TaskDbHelper class
    private var requestQueue: RequestQueue? = null
    private  lateinit var binding:ActivityMainBinding
    //private val client= OkHttpClient()
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
        val request = JsonObjectRequest(Request.Method.GET,
            url, null ,{ response ->
            try {
                Log.i("respo", "true $response")
                val jsonArray = response.getJSONObject("results").getJSONArray("datetime")
                Log.i("respo", "leng = ${jsonArray.length()} json =  ${jsonArray.getJSONObject(0).getJSONObject("times").getString("Fajr")}")

                val age = jsonArray.getJSONObject(0).getJSONObject("times").getString("Fajr")
                binding.country.text=jsonArray.getJSONObject(0).getJSONObject("times").getString("Sunrise")

            } catch (e: JSONException) { e.printStackTrace() }
        }, { error ->
            Log.i("respo", "false")
            error.printStackTrace()})
        val requestQueue:RequestQueue = Volley.newRequestQueue(this)
        requestQueue.add(request)
    }


    fun <T> T.lazyLog(tag: String = "LAZY_LOG"): T {
        Log.i(tag, toString())
        return this
    }
}



