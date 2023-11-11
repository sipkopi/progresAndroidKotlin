package com.rival.tutorialloginregist

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat

class WeatherActivity : AppCompatActivity() {
    private lateinit var etCity: EditText
    private lateinit var tvResult: TextView
    private val url = "https://api.openweathermap.org/data/2.5/weather"
    private val appid = "e53301e27efa0b66d05045d91b2742d3"
    private val df = DecimalFormat("#.##")

    // SharedPreferences
    private lateinit var sharedPreferences: SharedPreferences
    private val CITY_KEY = "city_key"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        etCity = findViewById(R.id.etCity)
        tvResult = findViewById(R.id.tvResult)

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Mendapatkan nilai dari SharedPreferences jika ada
        val savedCity = sharedPreferences.getString(CITY_KEY, "")
        etCity.setText(savedCity)
    }

    fun getWeatherDetails(view: View) {
        val city = etCity.text.toString().trim()

        if (city == "") {
            tvResult.text = "City field cannot be empty!"
        } else {
            // Simpan nilai "Sempol" ke SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString(CITY_KEY, city)
            editor.apply()

            val tempUrl = "$url?q=$city&appid=$appid"

            val stringRequest = StringRequest(Request.Method.POST, tempUrl, Response.Listener { response ->
                var output = ""
                try {
                    val jsonResponse = JSONObject(response)
                    val jsonArray = jsonResponse.getJSONArray("weather")
                    val jsonObjectWeather = jsonArray.getJSONObject(0)
                    val description = jsonObjectWeather.getString("description")
                    val jsonObjectMain = jsonResponse.getJSONObject("main")
                    val temp = jsonObjectMain.getDouble("temp") - 273.15
                    val feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15
                    val pressure = jsonObjectMain.getInt("pressure")
                    val humidity = jsonObjectMain.getInt("humidity")
                    val jsonObjectWind = jsonResponse.getJSONObject("wind")
                    val wind = jsonObjectWind.getString("speed")
                    val jsonObjectClouds = jsonResponse.getJSONObject("clouds")
                    val clouds = jsonObjectClouds.getString("all")
                    val jsonObjectSys = jsonResponse.getJSONObject("sys")
                    val countryName = jsonObjectSys.getString("country")
                    val cityName = jsonResponse.getString("name")
                    tvResult.setTextColor(Color.rgb(68, 134, 199))
                    output += "Current weather of $cityName ($countryName)" +
                            "\n Temp: ${df.format(temp)} °C" +
                            "\n Feels Like: ${df.format(feelsLike)} °C" +
                            "\n Humidity: $humidity%" +
                            "\n Description: $description" +
                            "\n Wind Speed: $wind m/s (meters per second)" +
                            "\n Cloudiness: $clouds%" +
                            "\n Pressure: $pressure hPa"
                    tvResult.text = output
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error ->
                Toast.makeText(applicationContext, error.toString().trim { it <= ' ' }, Toast.LENGTH_SHORT).show()
            })

            val requestQueue = Volley.newRequestQueue(applicationContext)
            requestQueue.add(stringRequest)
        }
    }
}
