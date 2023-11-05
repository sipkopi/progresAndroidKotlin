package com.rival.tutorialloginregist

import android.content.Intent
import android.os.Bundle
import android.view.View
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
        val packageManager = packageManager
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.setPackage("com.whatsapp")
        intent.putExtra(Intent.EXTRA_TEXT, messageTxt)

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(
                this,
                "Please install WhatsApp first.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

