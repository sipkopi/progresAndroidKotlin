package com.rival.tutorialloginregist.ProfilePage
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView // tambahkan import ini
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.rival.tutorialloginregist.R

class EditProfile : AppCompatActivity() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    private lateinit var textViewNama: TextView
    private lateinit var textViewEmail : TextView
    private lateinit var textViewNoHp : TextView// tambahkan TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso) // gunakan 'this' daripada 'requireActivity()' untuk Activity

        textViewNama = findViewById(R.id.editTextText)
        textViewEmail = findViewById(R.id.editTextTextEmailAddress)
        textViewNoHp = findViewById(R.id.editTextPhone)
    }

    override fun onStart() {
        super.onStart()

        val user = mAuth.currentUser

        if (user != null) {
            val userName = user.displayName
            val userEmail = user.email
            val userPhoneNumber = user.phoneNumber
            textViewEmail.text = "$userEmail"
            textViewNama.text = "$userName"
            textViewNoHp.text = "$userPhoneNumber"
        } else {

        }
    }
}