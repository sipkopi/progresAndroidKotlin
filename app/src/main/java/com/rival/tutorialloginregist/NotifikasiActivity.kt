package com.rival.tutorialloginregist

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rival.tutorialloginregist.databinding.ActivityNotifikasiBinding
import java.util.*

class NotifikasiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotifikasiBinding
    private lateinit var dbHelper: NotificationDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotifikasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = NotificationDbHelper(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        binding.submitButton.setOnClickListener { scheduleNotification() }
    }

    private fun scheduleNotification() {
        val title = binding.titleET.text.toString()
        val message = binding.messageET.text.toString()

        if (title.isEmpty() || message.isEmpty()) {
            return
        }

        val intent = Intent(applicationContext, Notification::class.java)
        intent.action = "com.rival.tutorialloginregist.SHOW_NOTIFICATION"
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val canScheduleExactAlarms = alarmManager.canScheduleExactAlarms()
            if (canScheduleExactAlarms) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    pendingIntent
                )
                showAlert(time, title, message)
            } else {
                // Handle jika perangkat tidak mendukung alarm exact
            }
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)
            showAlert(time, title, message)
        }

        dbHelper.insertNotification(title, message, time)
    }

    private fun showAlert(time: Long, title: String, message: String) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

        AlertDialog.Builder(this)
            .setTitle("Notification Scheduled")
            .setMessage(
                "Title: $title\nMessage: $message\nAt: ${dateFormat.format(date)} ${timeFormat.format(date)}"
            )
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }

    private fun getTime(): Long {
        val minute = binding.timePicker.minute
        val hour = binding.timePicker.hour
        val day = binding.datePicker.dayOfMonth
        val month = binding.datePicker.month
        val year = binding.datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
    }

    private fun createNotificationChannel() {
        val name = "Notif Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(NotificationDbHelper.CHANNEL_ID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}