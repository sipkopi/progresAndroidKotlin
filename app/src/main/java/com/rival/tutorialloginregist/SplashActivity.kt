package com.rival.tutorialloginregist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
class SplashActivity : AppCompatActivity() {
    private val splashTimeOut: Long = 2000 // Waktu tampilan splash screen dalam milidetik (3 detik)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash) // Gunakan layout splash screen yang sudah Anda buat

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java) // Ganti MainActivity dengan activity tujuan setelah splash screen
            startActivity(intent)
            finish()
        }, splashTimeOut)
    }
}