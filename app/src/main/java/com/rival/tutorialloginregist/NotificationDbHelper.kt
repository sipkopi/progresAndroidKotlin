package com.rival.tutorialloginregist

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NotificationDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "notification_database"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "notifications"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_MESSAGE = "message"
        const val COLUMN_TIME = "time"
        const val CHANNEL_ID = "channel1"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT,
                $COLUMN_MESSAGE TEXT,
                $COLUMN_TIME INTEGER
            )
        """.trimIndent()

        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Tidak ada implementasi khusus untuk upgrade database pada versi pertama
    }

    fun insertNotification(title: String, message: String, time: Long) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_MESSAGE, message)
            put(COLUMN_TIME, time)
        }

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllNotifications(): List<NotificationEntity> {
        val notifications = mutableListOf<NotificationEntity>()
        val db = readableDatabase
        val cursor: Cursor? = db?.query(
            TABLE_NAME, null, null, null, null, null,
            "$COLUMN_TIME DESC"
        )

        cursor?.use {
            while (it.moveToNext()) {
                val title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE))
                val message = it.getString(it.getColumnIndexOrThrow(COLUMN_MESSAGE))
                val time = it.getString(it.getColumnIndexOrThrow(COLUMN_TIME))
                notifications.add(NotificationEntity(title, message, time))
            }
        }

        return notifications
    }

    fun deleteNotification(title: String, message: String) {
        val db = writableDatabase
        db.delete(
            TABLE_NAME,
            "$COLUMN_TITLE = ? AND $COLUMN_MESSAGE = ?",
            arrayOf(title, message)
        )
        db.close()
    }
}
