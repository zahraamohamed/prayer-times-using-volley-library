package com.example.myapplication.data.ui

import android.os.Bundle
import android.util.Log
import android.webkit.*
import android.widget.SearchView
import android.widget.Toast
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
        searchCountry()
    }


    private fun makeRequest(country: String) {

        val url = "https://api.pray.zone/v2/times/today.json?city=$country"
        val request = JsonObjectRequest(Request.Method.GET,
            url, null ,{ response ->
            try {
                val jsonArray = response.getJSONObject("results").getJSONArray("datetime")
                val location=response.getJSONObject("results").getJSONObject("location")
                val age = jsonArray.getJSONObject(0).getJSONObject("times").getString("Fajr")
                binding.fajer.text=jsonArray.getJSONObject(0).getJSONObject("times").getString("Dhuhr")
                binding.dhuhr.text=jsonArray.getJSONObject(0).getJSONObject("times").getString("Asr")
                binding.asr.text=jsonArray.getJSONObject(0).getJSONObject("times").getString("Maghrib")
                binding.maghrib.text=jsonArray.getJSONObject(0).getJSONObject("times").getString("Isha")
                binding.isha.text=jsonArray.getJSONObject(0).getJSONObject("times").getString("Fajr")
                binding.country.text=location.getString("country")
                binding.city.text=location.getString("city")
                binding.date.text=jsonArray.getJSONObject(0).getJSONObject("date").getString("gregorian")

            } catch (e: JSONException) { e.printStackTrace() }
        }, { error ->

            error.printStackTrace()})
        val requestQueue:RequestQueue = Volley.newRequestQueue(this)
        requestQueue.add(request)
    }
    // search of country
    private fun  searchCountry(){
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                makeRequest(query!!.lowercase(Locale.getDefault()))
                if(query.isNullOrEmpty() )

                {
                    Toast.makeText(applicationContext, "country not found", Toast.LENGTH_LONG).show()

                }else {
                    makeRequest(query.lowercase(Locale.getDefault()))
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return false
            }

        })
    }



    fun <T> T.lazyLog(tag: String = "LAZY_LOG"): T {
        Log.i(tag, toString())
        return this
    }
}



