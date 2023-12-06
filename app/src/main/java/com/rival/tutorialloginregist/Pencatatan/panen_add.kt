package com.rival.tutorialloginregist.Pencatatan

import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.rival.tutorialloginregist.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class panen_add : AppCompatActivity() {
    val pengolahan = arrayOf("Full Wash", "Natural")
    val weight = arrayOf("100", "250", "500", "750", "1000")
    private lateinit var buttonDatePicker1: Button
    private lateinit var buttonDatePicker2: Button
    private lateinit var buttonDatePicker3: Button
    private lateinit var buttonpilihGambar2: Button
    private lateinit var imageFoto: ImageView
    private lateinit var tglPanen: EditText
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null
    private lateinit var tglRoasting: EditText
    private lateinit var kodePeremajaan: EditText
    private lateinit var deskripsi: EditText
    private lateinit var spinnerMetodePengolahan: Spinner
    private lateinit var spinnerWeight: Spinner
    private lateinit var Varietas: EditText
    private lateinit var tglExpired: EditText
    private val calendar = Calendar.getInstance()

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panen_add)
        spinnerMetodePengolahan = findViewById(R.id.Mtd_pengolahan)
        spinnerWeight = findViewById(R.id.Weight)

        val arrayAdapterMetodePengolahan =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, pengolahan)
        spinnerMetodePengolahan.adapter = arrayAdapterMetodePengolahan
        spinnerMetodePengolahan.onItemSelectedListener =
            createOnItemSelectedListener("Metode Pengolahan")

        val arrayAdapterWeight =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, weight)
        spinnerWeight.adapter = arrayAdapterWeight
        spinnerWeight.onItemSelectedListener = createOnItemSelectedListener("Weight")

        buttonDatePicker1 = findViewById(R.id.buttonDatePicker1)
        buttonDatePicker2 = findViewById(R.id.buttonDatePicker2)
        kodePeremajaan = findViewById(R.id.editTextText5)
        deskripsi = findViewById(R.id.editTextText6)
        buttonDatePicker3 = findViewById(R.id.buttonDatePicker3)
        tglPanen = findViewById(R.id.editTextDate2)
        tglRoasting = findViewById(R.id.editTextDate3)
        tglExpired = findViewById(R.id.editTextDate4)
        Varietas = findViewById(R.id.editTextText10)
        imageFoto = findViewById(R.id.imageView30)
        buttonpilihGambar2 = findViewById(R.id.pilihgambar2)

        buttonpilihGambar2.setOnClickListener {
            // Memilih gambar dari penyimpanan perangkat
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
        val submitButton: Button = findViewById(R.id.button3)
        submitButton.setOnClickListener {
            savePanenToServer()
        }

        buttonDatePicker1.setOnClickListener {
            showDatePicker(tglPanen)
        }
        buttonDatePicker2.setOnClickListener {
            showDatePicker(tglRoasting)
        }
        buttonDatePicker3.setOnClickListener {
            showDatePicker(tglExpired)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Mengambil URI gambar yang dipilih
            imageUri = data.data

            // Ubah URI menjadi path file yang sesuai
            val imageFile = imageUri?.let { File(getRealPathFromURI(it)) }

            // Menampilkan gambar pada ImageView
            imageFoto.setImageURI(imageUri)
        }
    }

    // Fungsi untuk mendapatkan path file dari URI
    private fun getRealPathFromURI(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val path = cursor?.getString(columnIndex ?: 0) ?: ""
        cursor?.close()
        return path
    }

    private fun savePanenToServer() {
        val url = "https://sipkopi.com/api/kopi/tambah.php"
        val varietas = Varietas.text.toString()
        val kodeKopi = ""
        val kodePeremajaanStr = kodePeremajaan.text.toString()
        val metodePengolahan = spinnerMetodePengolahan.selectedItem.toString()
        val berat = spinnerWeight.selectedItem.toString()
        val tanggalPanen = tglPanen.text.toString()
        val deskripsiStr = deskripsi.text.toString()
        val tanggalRoasting = tglRoasting.text.toString()
        val tanggalExpired = tglExpired.text.toString()
        val link = ""
        val gambarQr = ""

        try {
            val params = HashMap<String, String>()
            // Mengganti kode_kopi dan link sesuai kebutuhan
            params["kode_kopi"] = kodeKopi
            params["kode_peremajaan"] = kodePeremajaanStr
            params["varietas_kopi"] = varietas
            params["metode_pengolahan"] = metodePengolahan
            params["tgl_panen"] = tanggalPanen
            params["tgl_roasting"] = tanggalRoasting
            params["tgl_exp"] = tanggalExpired
            params["berat"] = berat
            params["link"] = link
            params["deskripsi"] = deskripsiStr
            params["gambarqr"] = gambarQr

            val request = MultipartRequest1(
                url,
                this,
                params,
                imageUri,
                Response.Listener<String> { response ->
                    val result = response.toString()
                    Log.d("PanenAdd", "Response received: $result")
                    Toast.makeText(this, "Data Panen terkirim: $result", Toast.LENGTH_SHORT)
                        .show()
                },
                Response.ErrorListener { error ->
                    Log.e("PanenAdd", "Error sending simpan data", error)
                    Toast.makeText(
                        this,
                        "Gagal mengirim data panen: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )

            val requestQueue = Volley.newRequestQueue(this)
            requestQueue.add(request)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showDatePicker(editText: EditText) {
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                // Ketika tanggal dipilih, simpan nilai tanggal ke dalam EditText
                calendar.set(year, month, dayOfMonth)
                updateDateInView(editText)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun updateDateInView(editText: EditText) {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        editText.setText(sdf.format(calendar.time))
    }

    private fun createOnItemSelectedListener(spinnerName: String): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedValue = when (spinnerName) {
                    "Metode Pengolahan" -> pengolahan[p2]
                    "Weight" -> weight[p2]
                    else -> ""
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }
}
