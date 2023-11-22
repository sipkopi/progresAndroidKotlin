package com.rival.tutorialloginregist.ProfilePage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.rival.tutorialloginregist.R

class Pengaturan : AppCompatActivity() {
    private lateinit var aboutUs : TextView
    private lateinit var Privacy : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaturan)
        val Privacy =findViewById<TextView>(R.id.textView20)
        val aboutUs = findViewById<TextView>(R.id.textView19)

        aboutUs.setOnClickListener {
            val intent = Intent(this, AboutUs::class.java)
            startActivity(intent)
        }
        Privacy.setOnClickListener {
            val intent = Intent(this, privacypolicy::class.java)
            startActivity(intent)
        }
    }
}