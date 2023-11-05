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
import android.widget.TimePicker
import androidx.fragment.app.Fragment

class SettingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inisialisasi layout Fragment
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        // Di sini Anda dapat mengakses elemen-elemen UI dalam layout dan menambahkan perilaku yang sesuai.

        return view
    }
}
