package com.rival.tutorialloginregist.Pencatatan

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rival.tutorialloginregist.R

class peremajaan : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var pembibitanAdapter: PeremajaanAdapter
    private lateinit var dataQueue: RequestQueue
    private val sharedPreferencesFileName = "UserProfile"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_peremajaan)
        recyclerView = findViewById(R.id.rv_peremajaan)

        val panggilan = loadUserProfileFromSharedPreferences(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        pembibitanAdapter = PeremajaanAdapter(  this, mutableListOf())
        recyclerView.adapter = pembibitanAdapter

        dataQueue = Volley.newRequestQueue(this)
        fetchData(panggilan)

        val fab2 :FloatingActionButton = findViewById(R.id.fab_peremajaan)
        fab2.setOnClickListener {
            val intent = Intent (this,peremajaanadd::class.java)
            startActivity(intent)
        }
        }
    private fun loadUserProfileFromSharedPreferences(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE)
        val panggilan = sharedPreferences.getString("Panggilan", "")

        Log.d("peremajaan", "Panggilan from SharedPreferences: $panggilan")

        return panggilan.orEmpty()
    }
    private fun fetchData(sharedPreferencesKeyPanggilan: String) {
        val url = "https://sipkopi.com/api/peremajaan/getperemajaan.php?user=$sharedPreferencesKeyPanggilan"
        Log.d("peremajaan", "API Request URL: $url")
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val dataList = mutableListOf<PeremajaanModel>()
                for (i in 0 until response.length()) {
                    val dataJson = response.getJSONObject(i)
                    val data = PeremajaanModel(
                        dataJson.getString("perlakuan"),
                        dataJson.getString("kode_lahan"),
                        dataJson.getString("tanggal"),
                        dataJson.getString("pupuk"),
                        dataJson.getString("kebutuhan")
                    )
                    dataList.add(data)
                }
                pembibitanAdapter.updateData(dataList)
            },
            Response.ErrorListener { error ->

            }
        )
        dataQueue.add(jsonArrayRequest)
    }
}


