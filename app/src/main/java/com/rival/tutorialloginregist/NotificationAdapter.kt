package com.rival.tutorialloginregist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rival.tutorialloginregist.R

class NotificationAdapter(private val notifications: MutableList<NotificationItem>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    var onDeleteItemTextViewClick: ((position: Int) -> Unit)? = null

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        val datetimeTextView: TextView = itemView.findViewById(R.id.datetimeTextView)
        val deleteItemTextView: TextView = itemView.findViewById(R.id.tvkonfirmasi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notificationItem = notifications[position]
        holder.titleTextView.text = notificationItem.title
        holder.messageTextView.text = notificationItem.message
        holder.datetimeTextView.text = notificationItem.datetime // Menampilkan tanggal dan waktu

        // Menambahkan tindakan penghapusan saat "DeleteItem" ditekan
        holder.deleteItemTextView.setOnClickListener {
            onDeleteItemTextViewClick?.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return notifications.size
    }
}

