package com.rival.tutorialloginregist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rival.tutorialloginregist.databinding.ActivityNotifikasi2Binding
import java.text.SimpleDateFormat
import java.util.*

class NotifikasiActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityNotifikasi2Binding
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHelper: NotificationDbHelper
    private lateinit var notifications: MutableList<NotificationEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotifikasi2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.notificationRecyclerView

        // Inisialisasi SQLite Database Helper
        dbHelper = NotificationDbHelper(this)

        // Membaca semua notifikasi dari database SQLite
        notifications = dbHelper.getAllNotifications().map { notificationEntity ->
            try {
                val dateMillis = notificationEntity.time.toLong()
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Sesuaikan dengan format tanggal yang sesuai
                val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault()) // Sesuaikan dengan format waktu yang sesuai
                val datetime = "${dateFormat.format(dateMillis)} ${timeFormat.format(dateMillis)}"

                NotificationEntity(notificationEntity.title, notificationEntity.message, datetime)
            } catch (e: NumberFormatException) {
                // Tangani kesalahan jika terjadi masalah konversi
                android.util.Log.e("NotifikasiActivity2", "Error converting date: $e")
                NotificationEntity(notificationEntity.title, notificationEntity.message, "Invalid Date")
            }
        }.toMutableList()

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
        // Tangani penghapusan notifikasi dari database SQLite
        val notification = notifications[position]
        dbHelper.deleteNotification(notification.title, notification.message)

        notifications.removeAt(position)
        recyclerView.adapter?.notifyItemRemoved(position)
    }
}
