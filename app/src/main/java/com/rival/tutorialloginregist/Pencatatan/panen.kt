package com.rival.tutorialloginregist.Pencatatan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rival.tutorialloginregist.R

class panen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panen)

        val fab_panen : FloatingActionButton = findViewById(R.id.fab_panen)
        fab_panen.setOnClickListener{
            val intent = Intent (this, panen_add::class.java)
            startActivity(intent)
        }
    }
}