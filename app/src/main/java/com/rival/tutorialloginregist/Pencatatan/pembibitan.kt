package com.rival.tutorialloginregist.Pencatatan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rival.tutorialloginregist.R

class pembibitan : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var pembibitanAdapter: pembibitanAdapter
    private lateinit var dataQueue: RequestQueue

    // Define the SharedPreferences file name
    private val sharedPreferencesFileName = "UserProfile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembibitan)

        // Panggil metode untuk memuat profil pengguna dari SharedPreferences
        val panggilan = loadUserProfileFromSharedPreferences(this)

        recyclerView = findViewById(R.id.rv_pembibitan)
        recyclerView.layoutManager = LinearLayoutManager(this)
        pembibitanAdapter = pembibitanAdapter(this, mutableListOf())
        recyclerView.adapter = pembibitanAdapter

        dataQueue = Volley.newRequestQueue(this)
        fetchData(panggilan)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, pembibitanadd::class.java)
            startActivity(intent)
        }
    }

    private fun loadUserProfileFromSharedPreferences(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE)
        val panggilan = sharedPreferences.getString("Panggilan", "")

        Log.d("pembibitan", "Panggilan from SharedPreferences: $panggilan")

        return panggilan.orEmpty()
    }

    private fun fetchData(sharedPreferencesKeyPanggilan: String) {
        val url = "https://sipkopi.com/api/lahan/getlahan.php?user=$sharedPreferencesKeyPanggilan"

        Log.d("pembibitan", "API Request URL: $url")

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                Log.d("pembibitan", "API Response: $response")

                val dataList = mutableListOf<lahanModel>()
                for (i in 0 until response.length()) {
                    val dataJson = response.getJSONObject(i)
                    val data = lahanModel(
                        dataJson.getString("kode_lahan"),
                        dataJson.getString("user"),
                        dataJson.getString("varietas_pohon"),
                        dataJson.getString("total_bibit"),
                        dataJson.getString("tanggal")
                    )
                    dataList.add(data)
                }

                Log.d("pembibitan", "Data List Size: ${dataList.size}")

                pembibitanAdapter.updateData(dataList)
            },
            Response.ErrorListener { error ->
                Log.e("pembibitan", "Error fetching data from API", error)
                // Handle error response here
            }
        )
        dataQueue.add(jsonArrayRequest)
    }
}
