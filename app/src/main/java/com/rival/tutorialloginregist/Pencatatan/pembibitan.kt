package com.rival.tutorialloginregist.Pencatatan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rival.tutorialloginregist.R

class pembibitan : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembibitan)

        val fab : FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener{
            val intent = Intent (this, pembibitanadd::class.java)
            startActivity(intent)
        }
    }
}