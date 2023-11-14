package com.rival.tutorialloginregist

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import com.rival.tutorialloginregist.Pencatatan.panen_add
import com.rival.tutorialloginregist.Pencatatan.pembibitanadd
import com.rival.tutorialloginregist.Pencatatan.peremajaanadd

class SettingFragment : Fragment() {

    private lateinit var txPembibitan : TextView
    private lateinit var txPerawatan : TextView
    private lateinit var txPanen : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inisialisasi layout Fragment
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        txPembibitan = view.findViewById(R.id.txPembibitan)
        txPerawatan = view.findViewById(R.id.txPeremajaan)
        txPanen = view.findViewById(R.id.txPanen)


        txPembibitan.setOnClickListener {
            val intent = Intent(activity, pembibitanadd::class.java)
            startActivity(intent)
        }
        txPerawatan.setOnClickListener {
            val intent = Intent(activity, peremajaanadd::class.java)
            startActivity(intent)
        }
        txPanen.setOnClickListener {
            val intent = Intent(activity, panen_add::class.java)
            startActivity(intent)
        }

        return view
    }
}
