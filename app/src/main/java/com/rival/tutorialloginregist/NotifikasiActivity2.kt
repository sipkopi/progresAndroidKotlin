package com.rival.tutorialloginregist

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rival.tutorialloginregist.databinding.ActivityNotifikasi2Binding

class NotifikasiActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityNotifikasi2Binding
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotifikasi2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.notificationRecyclerView

        // Baca data notifikasi dari SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("NotificationData", Context.MODE_PRIVATE)
        val title = sharedPreferences.getString("title", "")
        val message = sharedPreferences.getString("message", "")

        val notifications = mutableListOf<NotificationItem>()

        // Cek apakah terdapat data notifikasi yang sudah tersimpan
        if (!title.isNullOrEmpty() && !message.isNullOrEmpty()) {
            notifications.add(NotificationItem(title, message))
        }

        // Set up RecyclerView
        val adapter = NotificationAdapter(notifications)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
