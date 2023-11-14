package com.rival.tutorialloginregist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Pengaturan : AppCompatActivity() {
    private lateinit var aboutUs : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaturan)

        val aboutUs = findViewById<TextView>(R.id.textView19)

        aboutUs.setOnClickListener {
            val intent = Intent(this, AboutUs::class.java)
            startActivity(intent)
        }

    }
}