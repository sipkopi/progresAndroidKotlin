package com.rival.tutorialloginregist

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmReceiver", "Alarm received at ${System.currentTimeMillis()}")

        val temperature = intent.getStringExtra("temperature")
        val location = intent.getStringExtra("location")

        // Pengecekan null untuk menghindari NullPointerException
        if (temperature != null && location != null) {
            // Panggil metode untuk menampilkan notifikasi
            showNotification(context, temperature, location)
        }
    }

    private fun showNotification(context: Context, temperature: String, location: String) {
        val channelId = "weather_channel"
        val notificationId = 1

        // Buat notifikasi
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.logokopi)
            .setContentTitle("Peringatan Cuaca")
            .setContentText("Suhu: $temperature, Lokasi: $location")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Tampilkan notifikasi
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(notificationId, builder.build())
    }
}
