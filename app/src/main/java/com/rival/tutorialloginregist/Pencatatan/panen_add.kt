package com.rival.tutorialloginregist.Pencatatan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.rival.tutorialloginregist.R

class panen_add : AppCompatActivity() {
    val pengolahan = arrayOf("Proses basah", "Giling Basah","Proses Kering","Pulped Natural")
    val weight = arrayOf("100", "250", "500", "1000")
    val size = arrayOf("fine","medium","coarse","bean")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panen_add)
        val spinnerPerlakuan = findViewById<Spinner>(R.id.Mtd_pengolahan)
        val arrayAdapterJmlhtnm = ArrayAdapter(this, android.R.layout.simple_spinner_item, pengolahan)
        spinnerPerlakuan.adapter = arrayAdapterJmlhtnm
        spinnerPerlakuan.onItemSelectedListener = createOnItemSelectedListener("Metode Pengolahan")

        val spinnerJmlhtnm = findViewById<Spinner>(R.id.Weight)
        val arrayJmlhtnm = ArrayAdapter(this, android.R.layout.simple_spinner_item, weight)
        spinnerJmlhtnm.adapter = arrayJmlhtnm
        spinnerJmlhtnm.onItemSelectedListener = createOnItemSelectedListener("Weight")

        val spinnerSize = findViewById<Spinner>(R.id.Grand_sz)
        val arrayAdapterSize = ArrayAdapter(this, android.R.layout.simple_spinner_item, size)
        spinnerSize.adapter = arrayAdapterSize
        spinnerSize.onItemSelectedListener = createOnItemSelectedListener("Size")
    }

    private fun createOnItemSelectedListener(spinnerName: String): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedValue = when (spinnerName) {
                    "Metode Pengolahan" -> pengolahan[p2]
                    "Weight" -> weight[p2]
                    "Size" -> size[p2]
                    else -> ""
                }
                //Toast.makeText(this@panen_add, "Anda memilih $spinnerName: $selectedValue", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Tidak ada yang dipilih
            }
        }
    }
}