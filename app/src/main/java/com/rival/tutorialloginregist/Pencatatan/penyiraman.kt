package com.rival.tutorialloginregist.Pencatatan

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.set
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.rival.tutorialloginregist.R
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class penyiraman : Fragment() {
    private lateinit var buttonDatePicker1: Button
    private val calendar = Calendar.getInstance()
    private lateinit var editTextDate: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_penyiraman, container, false)

        val buttonSubmit = view.findViewById<Button>(R.id.button2)
        editTextDate = view.findViewById(R.id.editTextDate)
        buttonSubmit.setOnClickListener {
            // Panggil metode untuk mengirim data pemupukan
            sendPemupukanData()
        }
        buttonDatePicker1 = view.findViewById(R.id.buttonDatePicker1)
        buttonDatePicker1.setOnClickListener {
            showDatePicker()
        }
        return view
    }
    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
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
        editTextDate.setText(sdf.format(calendar.time))
    }

    private fun sendPemupukanData() {
        val url = "https://sipkopi.com/api/peremajaan/tambahv1.php"
        val kodeLahan = view?.findViewById<EditText>(R.id.editTextText2)
        val Perlakuan = view?.findViewById<TextView>(R.id.editTextText8)
        Perlakuan?.isEnabled = false
        val Tanggal = view?.findViewById<EditText>(R.id.editTextDate)
        val Kebutuhan = view?.findViewById<EditText>(R.id.editTextNumber2)



        val params = JSONObject()
        params.put("kode_lahan", kodeLahan?.text.toString())
        params.put("perlakuan", Perlakuan?.text.toString())
        params.put("tanggal", Tanggal?.text.toString())
        params.put("kebutuhan", Kebutuhan?.text.toString())



        val request = MyVolleyRequest(requireContext())

        val successListener = Response.Listener<JSONObject> { response ->
            // Handle the response
            // Misalnya, dapatkan data dari objek JSON
            val result = response.getString("result")
            // Lakukan sesuatu dengan hasil
            Toast.makeText(requireContext(), "Data pemupukan terkirim: $result", Toast.LENGTH_SHORT).show()
        }

        val errorListener = Response.ErrorListener { error ->
            Log.e(MyVolleyRequest.TAG, "Error sending pemupukan data", error)
        }


        request.postRequest(url, params, successListener, errorListener)
    }
}
