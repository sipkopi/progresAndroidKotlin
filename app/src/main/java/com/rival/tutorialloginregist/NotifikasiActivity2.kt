package com.rival.tutorialloginregist

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rival.tutorialloginregist.databinding.ActivityNotifikasi2Binding
import java.util.Date

class NotifikasiActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityNotifikasi2Binding
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotifikasi2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.notificationRecyclerView

        // Baca data notifikasi dari SharedPreferences
        val sharedPreferences = getSharedPreferences("NotificationData", Context.MODE_PRIVATE)

        // Membaca semua notifikasi dari SharedPreferences
        val notificationsSet = sharedPreferences.getStringSet("notifications", HashSet<String>()) ?: HashSet()
        val notifications = mutableListOf<NotificationItem>()

        for (notificationStr in notificationsSet) {
            val notificationData = notificationStr.split(",")
            if (notificationData.size == 3) {
                val title = notificationData[0]
                val message = notificationData[1]
                val time = notificationData[2].toLong()

                val date = Date(time)
                val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
                val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)
                val datetime = "${dateFormat.format(date)} ${timeFormat.format(date)}"

                notifications.add(NotificationItem(title, message, datetime))
            }
        }

        // Set up RecyclerView
        val adapter = NotificationAdapter(notifications)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
