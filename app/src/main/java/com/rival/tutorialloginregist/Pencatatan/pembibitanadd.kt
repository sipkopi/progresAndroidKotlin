package com.rival.tutorialloginregist.Pencatatan

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.rival.tutorialloginregist.R
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class pembibitanadd : AppCompatActivity() {

    private lateinit var textViewUser: TextView
    private lateinit var textViewVarietas: TextView
    private lateinit var textViewTotalBibit: TextView
    private lateinit var textViewLuas: TextView
    private lateinit var textViewTanggal: TextView
    private lateinit var textViewKetinggian: TextView
    private lateinit var textViewLat: TextView
    private lateinit var textViewLokasi: TextView
    private lateinit var textViewLong: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembibitanadd)

        val sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val storedUserName = sharedPreferences.getString("Panggilan", "user")
        val userNameTextView = findViewById<TextView>(R.id.editTextText3)
        userNameTextView.text = storedUserName
        userNameTextView.isEnabled = false

        textViewUser = findViewById(R.id.editTextText3)
        textViewVarietas = findViewById(R.id.editTextText4)
        textViewTotalBibit = findViewById(R.id.editTextNumber4)
        textViewLuas = findViewById(R.id.editTextText7)
        textViewTanggal = findViewById(R.id.editTextDate)
        textViewKetinggian = findViewById(R.id.editTextNumber3)
        textViewLokasi = findViewById(R.id.editTextText2)

        val buttonSaveProfile2 = findViewById<Button>(R.id.button2)
        buttonSaveProfile2.setOnClickListener {
            saveUserProfileToServer()
        }

    }
    fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
    private fun saveUserProfileToServer() {
        val url = "https://sipkopi.com/api/lahan/tambah.php"
        val userName = textViewUser.text.toString()
        val Varietas = textViewVarietas.text.toString()
        val totalbibit = textViewTotalBibit.text.toString()
        val luas = textViewLuas.text.toString()
        val tanggal = getCurrentDateTime()
        val ketinggian = textViewKetinggian.text.toString()
        val lokasi = textViewLokasi.text.toString()
        val kodelahan = ""
        val lat = "114.1677159"
        val long = "-8.0522759"

        if (userName.isNotBlank() && Varietas.isNotBlank() && totalbibit.isNotBlank() &&
            luas.isNotBlank() && ketinggian.isNotBlank() && lokasi.isNotBlank()) {

            val params = JSONObject()
            params.put("user", userName)
            params.put("varietas_pohon", Varietas)
            params.put("total_bibit", totalbibit)
            params.put("luas_lahan", luas)
            params.put("tanggal", tanggal)
            params.put("ketinggian_tanam", ketinggian)
            params.put("lokasi_lahan", lokasi)
            params.put("kode_lahan", kodelahan)
            params.put("longtitude", lat)
            params.put("latitude", long)

            val request = JsonObjectRequest(
                Request.Method.POST, url, params,
                Response.Listener { response ->
                    val result = response.getString("message")
                    Toast.makeText(this, "Data terkirim: $result", Toast.LENGTH_SHORT).show()
                },
                Response.ErrorListener { error ->
                    Log.e(MyVolleyRequest.TAG, "Error sending data", error)
                    Toast.makeText(this, "Gagal mengirim data", Toast.LENGTH_SHORT).show()
                }
            )

            Volley.newRequestQueue(this).add(request)
        } else {
            Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
        }
    }

}