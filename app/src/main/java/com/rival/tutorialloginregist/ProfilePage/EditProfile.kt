package com.rival.tutorialloginregist.ProfilePage
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.rival.tutorialloginregist.Pencatatan.MyVolleyRequest
import com.rival.tutorialloginregist.R
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditProfile : AppCompatActivity() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    private lateinit var textViewNama: TextView
    private lateinit var textViewEmail: TextView
    private lateinit var textViewNoHp: TextView
    private lateinit var textViewpPanggilan: TextView
    private lateinit var textViewpAlamat: TextView
    private lateinit var btnUnggah: Button
    private lateinit var imgFoto: ImageView

    private val sharedPreferencesFileName = "UserProfile"
    private val sharedPreferencesKeyUserName = "UserName"
    private val sharedPreferencesKeyUserEmail = "UserEmail"
    private val sharedPreferencesKeyUserPhoneNumber = "UserPhoneNumber"
    private val sharedPreferencesKeyPanggilan = "Panggilan"
    private val sharedPreferencesKeyAlamat = "Alamat"
    private val PICK_IMAGE_REQUEST = 2
    private val sharedPreferencesKeyUserFoto = "UserFoto"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        textViewNama = findViewById(R.id.editTextText11)
        textViewEmail = findViewById(R.id.editTextPhone)
        textViewNoHp = findViewById(R.id.editTextTextPostalAddress)
        textViewpPanggilan = findViewById(R.id.editTextText)
        textViewpAlamat = findViewById(R.id.editTextTextEmailAddress)
        btnUnggah = findViewById(R.id.btnUnggah)
        imgFoto = findViewById(R.id.imgFoto)

        btnUnggah.setOnClickListener {
            pickImageFromGallery()
        }

        val buttonSaveProfile = findViewById<Button>(R.id.buttonSimpan3)
        buttonSaveProfile.setOnClickListener {
            saveUserProfileToServer()
        }
    }

    override fun onStart() {
        super.onStart()

        // Hapus data dari Shared Preferences jika Anda ingin memastikan data diperbarui dari Firebase
        // clearDataFromSharedPreferences()

        loadUserProfileFromSharedPreferences()

        val user = mAuth.currentUser

        if (user != null) {
            // Proses login berhasil
            user.getIdToken(true).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Token berhasil diperbarui, dapatkan data pengguna
                    val refreshedUser = mAuth.currentUser
                    val userName = refreshedUser?.displayName
                    val userEmail = refreshedUser?.email


                    // Setel data dari Firebase ke tampilan
                    textViewEmail.text = "$userEmail"
                    textViewNama.text = "$userName"

                } else {

                    Log.e("EditProfile", "Gagal memperbarui token", task.exception)
                }
            }
        } else {
            // Handle when the user is not logged in
        }
    }

    override fun onResume() {
        super.onResume()
        loadUserProfileFromSharedPreferences()
        loadImageFromInternalStorage()  // Panggil fungsi untuk memuat gambar dari penyimpanan internal
    }
    private fun loadUserProfileFromSharedPreferences() {
        val sharedPreferences =
            getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString(sharedPreferencesKeyUserName, "")
        val userEmail = sharedPreferences.getString(sharedPreferencesKeyUserEmail, "")
        val userPhoneNumber = sharedPreferences.getString(sharedPreferencesKeyUserPhoneNumber, "")
        val panggilan = sharedPreferences.getString(sharedPreferencesKeyPanggilan, "")
        val alamat = sharedPreferences.getString(sharedPreferencesKeyAlamat, "")

        textViewNama.text = userName
        textViewEmail.text = userEmail
        textViewNoHp.text = userPhoneNumber
        textViewpPanggilan.text = panggilan
        textViewpAlamat.text = alamat
    }

    fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun saveUserProfileToServer() {
        val url = "https://sipkopi.com/api/user/tambah.php"
        val userName = textViewNama.text.toString()
        val userEmail = textViewEmail.text.toString()
        val userPhoneNumber = textViewNoHp.text.toString()
        val panggilan = textViewpPanggilan.text.toString()
        val alamat = textViewpAlamat.text.toString()
        val pass = ""
        val level = "petani"
        val tanggalCreate = getCurrentDateTime()
        val gambar = ""

        if (userName.isNotBlank() && userEmail.isNotBlank() && userPhoneNumber.isNotBlank() &&
            panggilan.isNotBlank() && alamat.isNotBlank() && level.isNotBlank() && tanggalCreate.isNotBlank()
        ) {
            val params = JSONObject()
            params.put("user", panggilan)
            params.put("nama", userName)
            params.put("email", userEmail)
            params.put("nohp", userPhoneNumber)
            params.put("pass", pass)
            params.put("lokasi", alamat)
            params.put("level", level)
            params.put("tanggal_create", tanggalCreate)
            params.put("gambar", gambar)

            val request = JsonObjectRequest(Request.Method.POST, url, params,
                Response.Listener { response ->
                    val result = response.getString("message")
                    Toast.makeText(this, "Data Profile terkirim: $result", Toast.LENGTH_SHORT)
                        .show()

                    // Menyimpan data ke SharedPreferences setelah berhasil terkirim
                    saveDataToSharedPreferences(
                        userName,
                        userEmail,
                        userPhoneNumber,
                        panggilan,
                        alamat
                    )
                },
                Response.ErrorListener { error ->
                    Log.e(MyVolleyRequest.TAG, "Error sending simpan data", error)
                    Toast.makeText(this, "Gagal mengirim data profile", Toast.LENGTH_SHORT).show()
                }
            )

            Volley.newRequestQueue(this).add(request)
        } else {
            Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveDataToSharedPreferences(
        userName: String,
        userEmail: String,
        userPhoneNumber: String,
        panggilan: String,
        alamat: String
    ) {
        // Menyimpan data ke SharedPreferences
        val sharedPreferences =
            getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(sharedPreferencesKeyUserName, userName)
        editor.putString(sharedPreferencesKeyUserEmail, userEmail)
        editor.putString(sharedPreferencesKeyUserPhoneNumber, userPhoneNumber)
        editor.putString(sharedPreferencesKeyPanggilan, panggilan)
        editor.putString(sharedPreferencesKeyAlamat, alamat)
        editor.apply()
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data
            // Setel gambar yang dipilih ke ImageView
            imgFoto.setImageURI(selectedImageUri)

            saveImageToInternalStorage(selectedImageUri)
        }
    }

    private fun saveImageToInternalStorage(selectedImageUri: Uri?) {
        if (selectedImageUri != null) {
            val inputStream = contentResolver.openInputStream(selectedImageUri)
            val file = File(filesDir, "profile_image.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()

            // Simpan path file ke SharedPreferences
            saveImagePathToSharedPreferences(file.absolutePath)
        } else {
            // Handle ketika selectedImageUri null
            Log.e("EditProfile", "selectedImageUri is null")
        }
    }


    private fun saveImagePathToSharedPreferences(imagePath: String) {
        val sharedPreferences = getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(sharedPreferencesKeyUserFoto, imagePath)
        editor.apply()
    }

    private fun loadImageFromInternalStorage() {
        val sharedPreferences =
            getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE)
        val imagePath = sharedPreferences.getString(sharedPreferencesKeyUserFoto, "")

        if (!imagePath.isNullOrBlank()) {
            val file = File(imagePath)
            if (file.exists()) {
                val imageUri = Uri.fromFile(file)
                imgFoto.setImageURI(imageUri)
            }
        }
    }
}
