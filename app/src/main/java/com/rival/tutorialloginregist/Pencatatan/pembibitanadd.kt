package com.rival.tutorialloginregist.Pencatatan

import android.app.DatePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rival.tutorialloginregist.R
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.jar.Manifest

class pembibitanadd : AppCompatActivity() {

    private lateinit var textViewUser: TextView
    private lateinit var textViewVarietas: TextView
    private lateinit var textViewTotalBibit: TextView
    private lateinit var textViewLuas: TextView
    private lateinit var textViewTanggal: TextView
    private lateinit var textViewKetinggian: TextView
    private lateinit var textViewLat: TextView
    private lateinit var buttonDatePicker: Button
    private val calendar = Calendar.getInstance()
    private lateinit var textViewLokasi: TextView

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var editTextLatLong: TextView
    private lateinit var editTextLong : TextView


    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembibitanadd)

        val sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val storedUserName = sharedPreferences.getString("Panggilan", "user")
        val userNameTextView = findViewById<TextView>(R.id.editTextText3)
        userNameTextView.text = storedUserName
        userNameTextView.isEnabled = false


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        editTextLatLong = findViewById(R.id.longLat)
        editTextLong = findViewById(R.id.edtLat)
        textViewKetinggian = findViewById(R.id.editTextNumber3)
        editTextLong.isEnabled = false
        textViewKetinggian.isEnabled = false
        editTextLatLong.isEnabled = false


            requestLocationUpdates()

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Jika izin sudah diberikan, dapatkan lokasi terkini
            requestLocationUpdates()
        } else {
            // Jika izin belum diberikan, minta izin
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }


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
        buttonDatePicker = findViewById(R.id.buttonDatePicker)

        buttonDatePicker.setOnClickListener {
            showDatePicker()
        }
    }
    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                // Ketika tanggal dipilih, simpan nilai tanggal ke dalam EditText
                calendar.set(year, month, dayOfMonth)
                updateDateInView()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun updateDateInView() {
        val myFormat = "yyyy-MM-dd" // Format tanggal yang diinginkan
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textViewTanggal.setText(sdf.format(calendar.time))
    }
    private fun requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                // Handle lokasi terkini di sini
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val altitude = location.altitude.toInt()
                    val textAddress = getAddressName(latitude, longitude) + ",(Tambahkan Detail Lokasi)"
                    // Set nilai latitude dan longitude pada EditText
                    val latLongText = "$latitude"
                    val Longitude =  "$longitude"
                    editTextLong.setText(Longitude)
                    editTextLatLong.setText(latLongText)
                    textViewLokasi.text = textAddress
                    textViewKetinggian.text = altitude.toString()
                }
            }
            .addOnFailureListener { e ->
                Log.e("LocationUpdate", "Error getting location", e)
            }

    }
    private fun getAddressName(lat: Double, lon: Double): String {
        var addressName = ""
        val geoCoder = Geocoder(this, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, lon, 1)

        if (address != null && address.isNotEmpty()) {
            addressName = address[0].locality
        }

        return addressName
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Jika izin diberikan, dapatkan lokasi terkini
                requestLocationUpdates()
            } else {
                // Handle jika izin tidak diberikan
            }
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
        val lat = editTextLatLong.text.toString()
        val long = editTextLong.text.toString()

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