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

class panen : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var panenAdapter: PanenAdapter
    private lateinit var dataQueue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panen)
        recyclerView = findViewById(R.id.rv_panen)
        recyclerView.layoutManager = LinearLayoutManager(this)
        panenAdapter = PanenAdapter(this, mutableListOf())
        recyclerView.adapter = panenAdapter

        dataQueue = Volley.newRequestQueue(this)
        fetchData()

        val fab_panen : FloatingActionButton = findViewById(R.id.fab_panen)
        fab_panen.setOnClickListener{
            val intent = Intent (this, panen_add::class.java)
            startActivity(intent)
        }
    }
    private fun fetchData() {
        val url = "https://sipkopi.com/api/kopi/kopi.php"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val dataList = mutableListOf<PanenModel>()
                for (i in 0 until response.length()) {
                    val dataJson = response.getJSONObject(i)
                    val data = PanenModel(
                        dataJson.getString("kode_kopi"),
                        dataJson.getString("varietas_kopi"),
                        dataJson.getString("metode_pengolahan"),
                        dataJson.getString("tgl_roasting"),
                        dataJson.getString("tgl_panen")
                    )
                    dataList.add(data)
                }
                panenAdapter.updateData(dataList)
            },
            Response.ErrorListener { error ->
                // Handle errors, for example, show an error message
                // Toast.makeText(this, "Failed to fetch data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )
        dataQueue.add(jsonArrayRequest)
    }

}