package com.rival.tutorialloginregist

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.rival.tutorialloginregist.ProfilePage.EditProfile
import com.rival.tutorialloginregist.databinding.ActivityMainBinding
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {



    private lateinit var gSignInBtn: Button
    private val CHECK_EMAIL_REQUEST_CODE = 123
    private lateinit var dbHelper: SQLiteOpenHelper
    private lateinit var imgFoto: ImageView
    // Inisialisasi SessionManager
    private lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        gSignInBtn = binding.gSignInBtn1

        // Inisialisasi SessionManager
        sessionManager = SessionManager(this)

        dbHelper = NotificationDbHelper(this)

        firebaseAuth = FirebaseAuth.getInstance()

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        if (currentUser != null) {
            // The user is already signed in, navigate to MainActivity
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish() // finish the current activity to prevent the user from coming back to the SignInActivity using the back button
        }

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        gSignInBtn.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                // Periksa apakah user telah login sebelumnya
                if (auth.currentUser == null) {
                    // User belum login, lakukan autentikasi
                    firebaseAuthWithGoogle(account.idToken!!)
                } else {
                    // User sudah login sebelumnya, lanjutkan ke HomeActivity
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Signed in as ${user?.displayName}", Toast.LENGTH_SHORT)
                        .show()
                    checkEmailOnAPI(user?.email)
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
//  saveUserProfile(firstUser)
private fun checkEmailOnAPI(email: String?) {
    val url = "https://sipkopi.com/api/user/getemail.php"

    val params = hashMapOf("email" to email.orEmpty())

    val request = JsonArrayRequest(
        Request.Method.GET,
        "$url?email=${params["email"]}",
        null,
        { response ->
            try {
                // Handle response dari API
                if (response.length() > 0) {
                    // Ambil objek pertama dari array (anda dapat menyesuaikan dengan kebutuhan Anda)
                    val firstUser = response.getJSONObject(0)

                    // Cek apakah objek tidak kosong
                    if (firstUser.length() > 0) {
                        // Email terdaftar, lakukan sesuai dengan kebutuhan aplikasi Anda
                        saveUserProfile(firstUser)
                        // Contoh: Tampilkan pesan atau navigasi ke halaman berikutnya
                        Toast.makeText(this, "Selamat Datang Kembali!", Toast.LENGTH_SHORT).show()
                        // Jika perlu, simpan data pengguna ke SharedPreferences di sini

                    } else {
                        // Email belum terdaftar, tampilkan dialog
                        //showCompleteProfileDialog()
                        Toast.makeText(this, "Tolong Lengkapi Data Diri Anda Dahulu Pada Menu Profile!", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Log.d("JSON_RESPONSE", response.toString())
                    // Response array kosong, tampilkan dialog
                    Toast.makeText(this, "Tolong Lengkapi Data Diri Anda Dahulu Pada Menu Profile!", Toast.LENGTH_LONG).show()
                    //showCompleteProfileDialog()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                Toast.makeText(this, "Error parsing JSON", Toast.LENGTH_SHORT).show()
            }
        },
        { error ->
            // Handle error dari API
            Log.e("API_ERROR", "Error checking email on API: ${error.message}")
            Toast.makeText(this, "Error checking email on API: ${error.message}", Toast.LENGTH_SHORT).show()
        })

    // Tambahkan request ke queue Volley
    Volley.newRequestQueue(this).add(request)
}

    private fun showCompleteProfileDialog() {
        // Periksa apakah activity sudah dihancurkan atau tidak aktif
        if (isFinishing) {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Lengkapi Profil")
            alertDialogBuilder.setMessage("Profil Anda belum lengkap. Lengkapi sekarang?")
            alertDialogBuilder.setPositiveButton("Ya") { _, _ ->
                // Jika pengguna menekan "Ya", arahkan ke halaman EditProfile
                val intent = Intent(this, EditProfile::class.java)
                intent.putExtra("isEmailRegistered", false)
                startActivityForResult(intent, CHECK_EMAIL_REQUEST_CODE)
            }
            alertDialogBuilder.setNegativeButton("Tidak") { _, _ ->
                // Jika pengguna menekan "Tidak", bisa tambahkan logika lainnya atau biarkan kosong
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }




    private val sharedPreferencesFileName = "UserProfile"

    private fun saveUserProfile(user: JSONObject) {
        val sharedPreferences = getSharedPreferences(sharedPreferencesFileName, MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Simpan data pengguna ke dalam SharedPreferences
        editor.putString("UserName", user.getString("nama"))
        editor.putString("UserEmail", user.getString("email"))
        editor.putString("UserPhoneNumber", user.getString("nohp"))
        editor.putString("Panggilan", user.getString("user"))
        editor.putString("Alamat", user.getString("lokasi"))
        editor.putString("gambar", user.getString("gambar"))


        // Terapkan perubahan
        editor.apply()
    }


    private fun login(username: String, password: String): Boolean {
        val db = dbHelper.readableDatabase
        val columns = arrayOf("id")
        val selection = "username = ? AND password = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor: Cursor = db.query("user", columns, selection, selectionArgs, null, null, null)
        val count = cursor.count
        cursor.close()
        return count > 0
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
