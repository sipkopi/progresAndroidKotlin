package com.rival.tutorialloginregist


import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.rival.tutorialloginregist.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerName: EditText
    private lateinit var registerUsername: EditText
    private lateinit var registerPhone: EditText
    private lateinit var registerPassword: EditText
    private lateinit var registerButton: Button
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val dbHelper = NotificationDbHelper(this)

        binding.loginBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.registerButton1.setOnClickListener {
            val name = binding.registName.text.toString()
            val username = binding.registerUsername.text.toString()
            val phone = binding.registerPhone.text.toString()
            val password = binding.registerPassword.text.toString()

            if (registerUser(name, username, phone, password, dbHelper)) {
                Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                finish() // Kembali ke aktivitas login setelah registrasi berhasil
            } else {
                Toast.makeText(this, "Gagal melakukan registrasi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(
        name: String,
        username: String,
        phone: String,
        password: String,
        dbHelper: NotificationDbHelper
    ): Boolean {
        val values = ContentValues()
        values.put("name", name)
        values.put("username", username)
        values.put("phone", phone)
        values.put("password", password)

        val db = dbHelper.writableDatabase
        val newRowId = db.insert("user", null, values)
        db.close()

        return newRowId != -1L
    }
}
