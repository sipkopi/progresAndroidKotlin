package com.rival.tutorialloginregist.Pencatatan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rival.tutorialloginregist.R
import com.rival.tutorialloginregist.Pencatatan.pembibitanAdapter
import com.rival.tutorialloginregist.Pencatatan.lahanModel

class pembibitan : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var pembibitanAdapter: pembibitanAdapter
    private lateinit var dataQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembibitan)

        recyclerView = findViewById(R.id.rv_pembibitan)
        recyclerView.layoutManager = LinearLayoutManager(this)
        pembibitanAdapter = pembibitanAdapter(this, mutableListOf())
        recyclerView.adapter = pembibitanAdapter

        dataQueue = Volley.newRequestQueue(this)
        fetchData()

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, pembibitanadd::class.java)
            startActivity(intent)
        }
    }

    private fun fetchData() {
        val url = "https://sipkopi.com/api/lahan/lahan.php"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val dataList = mutableListOf<lahanModel>()
                for (i in 0 until response.length()) {
                    val dataJson = response.getJSONObject(i)
                    val data = lahanModel(
                        dataJson.getString("kode_lahan"),
                        dataJson.getString("user"),
                        dataJson.getString("varietas_pohon"),
                        dataJson.getString("total_bibit")
                    )
                    dataList.add(data)
                }
                pembibitanAdapter.updateData(dataList)
            },
            Response.ErrorListener { error ->
                // Tangani kesalahan permintaan di sini
            }
        )
        dataQueue.add(jsonArrayRequest)
    }
}
