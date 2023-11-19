package com.rival.tutorialloginregist.Pencatatan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rival.tutorialloginregist.R

class peremajaan : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_peremajaan)

        val fab2 :FloatingActionButton = findViewById(R.id.fab_peremajaan)
        fab2.setOnClickListener {
            val intent = Intent (this,peremajaanadd::class.java)
            startActivity(intent)
        }
        }
    }
