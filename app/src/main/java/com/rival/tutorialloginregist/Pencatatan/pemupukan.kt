package com.rival.tutorialloginregist.Pencatatan

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.rival.tutorialloginregist.R
import org.json.JSONObject

class pemupukan : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pemupukan, container, false)

        val buttonSubmit = view.findViewById<Button>(R.id.buttonSimpan2)
        buttonSubmit.setOnClickListener {
            // Panggil metode untuk mengirim data pemupukan
            sendPemupukanData()
        }

        return view
    }

    private fun sendPemupukanData() {
        val url = "https://sipkopi.com/api/peremajaan/tambahv1.php"
        val kodeLahan = view?.findViewById<EditText>(R.id.editTextText2)
        val Perlakuan = view?.findViewById<EditText>(R.id.editTextText8)
        val Tanggal = view?.findViewById<EditText>(R.id.editTextDate)
        val Kebutuhan = view?.findViewById<EditText>(R.id.editTextNumber2)
        val Pupuk = view?.findViewById<EditText>(R.id.editTextText9)


        val params = JSONObject()
        params.put("kode_lahan", kodeLahan?.text.toString())
        params.put("perlakuan", Perlakuan?.text.toString())
        params.put("tanggal", Tanggal?.text.toString())
        params.put("kebutuhan", Kebutuhan?.text.toString())
        params.put("pupuk", Pupuk?.text.toString())


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
