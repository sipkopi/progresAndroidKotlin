package com.rival.tutorialloginregist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ContactUs : AppCompatActivity() {

    private lateinit var message: EditText
    private lateinit var submit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        message = findViewById(R.id.message)
        submit = findViewById(R.id.submit)

        submit.setOnClickListener {
            val messageTxt = message.text.toString()
            sendMessage(messageTxt)
        }
    }

    private fun sendMessage(messageTxt: String) {
        val phone = "+6288803716911" // Replace with the recipient's phone number

        val intent = Intent(Intent.ACTION_VIEW)
        val url = "https://api.whatsapp.com/send?phone=$phone&text=$messageTxt"

        intent.data = Uri.parse(url)

        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Please install WhatsApp first.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
