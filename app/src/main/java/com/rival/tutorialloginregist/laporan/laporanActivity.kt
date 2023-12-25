package com.rival.tutorialloginregist.laporan

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.rival.tutorialloginregist.Pencatatan.pembibitanAdapter
import com.rival.tutorialloginregist.R
import com.rival.tutorialloginregist.laporan.laporanModel
import com.rival.tutorialloginregist.laporan.laporanAdapter

class laporanActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var laporanAdapter: laporanAdapter
    private lateinit var dataQueue: RequestQueue
    private val sharedPreferencesFileName = "UserProfile"
    private lateinit var jumlahHariTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan)
        val panggilan = loadUserProfileFromSharedPreferences(this)

        recyclerView = findViewById(R.id.rv_laporan)

        recyclerView.layoutManager = LinearLayoutManager(this)
        laporanAdapter = laporanAdapter(this, mutableListOf())
        recyclerView.adapter = laporanAdapter

        dataQueue = Volley.newRequestQueue(this)
        fetchData(panggilan)
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
                val dataList = mutableListOf<laporanModel>()
                for (i in 0 until response.length()) {
                    val dataJson = response.getJSONObject(i)
                    val data = laporanModel(
                        dataJson.getString("kode_lahan"),
                        dataJson.getString("user"),
                        dataJson.getString("varietas_pohon"),
                        dataJson.getString("total_bibit"),
                        dataJson.getString("tanggal")
                    )
                    dataList.add(data)
                }
                laporanAdapter.updateData(dataList)
            },
            Response.ErrorListener { error ->

            }
        )
        dataQueue.add(jsonArrayRequest)
    }
}
