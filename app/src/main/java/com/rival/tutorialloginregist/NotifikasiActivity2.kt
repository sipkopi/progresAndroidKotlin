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
    private lateinit var notifications: MutableList<NotificationItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotifikasi2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.notificationRecyclerView

        // Baca data notifikasi dari SharedPreferences
        val sharedPreferences = getSharedPreferences("NotificationData", Context.MODE_PRIVATE)

        // Membaca semua notifikasi dari SharedPreferences
        val notificationsSet = sharedPreferences.getStringSet("notifications", HashSet<String>()) ?: HashSet()
        notifications = mutableListOf()

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

        adapter.onDeleteItemTextViewClick = { position ->
            // Tangani penghapusan notifikasi saat "DeleteItem" TextView ditekan
            removeNotification(position)
        }
    }

    private fun removeNotification(position: Int) {
        val sharedPreferences = getSharedPreferences("NotificationData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val notification = notifications[position]
        val notificationStr = "${notification.title},${notification.message},${notification.datetime}"
        val notificationsSet = sharedPreferences.getStringSet("notifications", HashSet<String>()) ?: HashSet()
        notificationsSet.remove(notificationStr)

        editor.putStringSet("notifications", notificationsSet)
        editor.apply()

        notifications.removeAt(position)
        recyclerView.adapter?.notifyItemRemoved(position)
    }
}
