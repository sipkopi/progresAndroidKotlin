package com.rival.tutorialloginregist

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    val CITY: String = "bondowoso"
    val API: String = "cfcd8b7a6440333b004c5233853ecbb0" // Use API key

    val notificationHour: Int = 21
    val notificationMinute: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        weatherTask().execute()

    }

    inner class weatherTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            /* Showing the ProgressBar, Making the main design GONE */
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errorText).visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response: String?
            try {
                response =
                    URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(
                        Charsets.UTF_8
                    )
            } catch (e: Exception) {
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                /* Extracting JSON returns from the API */
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

                val updatedAt: Long = jsonObj.getLong("dt")
                val updatedAtText =
                    "Updated at: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                        Date(updatedAt * 1000)
                    )
                val temp = String.format(Locale.ENGLISH, "%.0f°C", main.getDouble("temp"))
                val tempMin = "Min Temp: " + String.format(Locale.ENGLISH, "%.0f°C", main.getDouble("temp_min"))
                val tempMax = "Max Temp: " + String.format(Locale.ENGLISH, "%.0f°C", main.getDouble("temp_max"))
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")

                val sunrise: Long = sys.getLong("sunrise")
                val sunset: Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")

                val address = jsonObj.getString("name") + ", " + sys.getString("country")

                /* Populating extracted data into our views */
                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.updated_at).text = updatedAtText
                findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.temp_min).text = tempMin
                findViewById<TextView>(R.id.temp_max).text = tempMax
                findViewById<TextView>(R.id.sunrise).text =
                    SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise * 1000))
                findViewById<TextView>(R.id.sunset).text =
                    SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset * 1000))
                findViewById<TextView>(R.id.wind).text = windSpeed
                findViewById<TextView>(R.id.pressure).text = pressure
                findViewById<TextView>(R.id.humidity).text = humidity

                /* Views populated, Hiding the loader, Showing the main design */
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE
                val calendar = Calendar.getInstance()
                val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
                val currentMinute = calendar.get(Calendar.MINUTE)

                if (currentHour == notificationHour && currentMinute == notificationMinute) {
                    // Panggil metode untuk menjadwalkan alarm sebagai notifikasi
                    scheduleAlarm(temp, address)
                }

            } catch (e: Exception) {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
            }

        }
    }
    private fun scheduleAlarm(temperature: String, location: String) {
        Log.d("WeatherActivity", "Alarm scheduled for $notificationHour:$notificationMinute")
        val intent = Intent(this, AlarmReceiver::class.java)

        intent.putExtra("temperature", temperature)
        intent.putExtra("location", location)

        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, notificationHour)
        calendar.set(Calendar.MINUTE, notificationMinute)
        calendar.set(Calendar.SECOND, 0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

                try {
                    val canScheduleExactAlarms = alarmManager.canScheduleExactAlarms()
                    if (canScheduleExactAlarms) {
                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            pendingIntent

                        )
                    } else {

                    }
                } catch (e: SecurityException) {

                    e.printStackTrace()

                }
            } else {

                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } else {

            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }
}
