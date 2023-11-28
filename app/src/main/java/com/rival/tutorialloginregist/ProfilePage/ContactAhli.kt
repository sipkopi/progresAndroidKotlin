package com.rival.tutorialloginregist.ProfilePage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.rival.tutorialloginregist.R

class ContactAhli : Fragment() {

    private lateinit var message: EditText
    private lateinit var submit: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact_ahli, container, false)

        message = view.findViewById(R.id.message1)
        submit = view.findViewById(R.id.submitt)

        submit.setOnClickListener {
            val messageTxt = message.text.toString()
            sendMessage(messageTxt)
        }

        return view
    }

    private fun sendMessage(messageTxt: String) {
        val phone = "+6288803716911" // Ganti dengan nomor telepon penerima

        val intent = Intent(Intent.ACTION_VIEW)
        val url = "https://api.whatsapp.com/send?phone=$phone&text=$messageTxt"

        intent.data = Uri.parse(url)

        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Silakan install WhatsApp terlebih dahulu.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
