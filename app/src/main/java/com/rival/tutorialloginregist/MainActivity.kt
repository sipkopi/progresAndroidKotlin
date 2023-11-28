package com.rival.tutorialloginregist

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.rival.tutorialloginregist.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var gSignInBtn: Button
    private lateinit var LupaSandiButton: TextView
    private lateinit var registerButton: TextView

    private lateinit var dbHelper: SQLiteOpenHelper

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
        startActivityForResult(signInIntent, MainActivity.RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MainActivity.RC_SIGN_IN) {
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
    private fun checkEmailOnAPI(email: String?) {
        val url = "https://sipkopi.com/api/user/datauser.php"

        val params = hashMapOf<String, String>()
        params["email"] = email.orEmpty()

        val request = JsonObjectRequest(
            Request.Method.POST, url, JSONObject(params.toMap()),
            { response ->
                // Handle response dari API
                val isEmailRegistered = response.getBoolean("is_email_registered")

                if (isEmailRegistered) {
                    // Email terdaftar, lakukan sesuai dengan kebutuhan aplikasi Anda
                    // Contoh: Tampilkan pesan atau navigasi ke halaman berikutnya
                    Toast.makeText(this, "Email sudah terdaftar", Toast.LENGTH_SHORT).show()
                } else {
                    // Email belum terdaftar, lakukan sesuai dengan kebutuhan aplikasi Anda
                    // Contoh: Lanjutkan proses login atau tampilkan pesan
                    Toast.makeText(this, "Email belum terdaftar", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                // Handle error dari API
                Toast.makeText(this, "Error checking email on API: ${error.message}", Toast.LENGTH_SHORT).show()
            })

        // Tambahkan request ke queue Volley
        Volley.newRequestQueue(this).add(request)
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
