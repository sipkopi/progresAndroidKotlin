package com.rival.tutorialloginregist

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.rival.tutorialloginregist.ProfilePage.ContactUs
import com.rival.tutorialloginregist.ProfilePage.EditProfile
import com.rival.tutorialloginregist.ProfilePage.Pengaturan

class Settings : Fragment() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    private lateinit var textView6 : TextView
    private lateinit var textView2 : TextView
    private lateinit var imageView4 : ImageView
    private lateinit var textView3 : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val textView = view.findViewById<TextView>(R.id.textView5)

        val user = mAuth.currentUser

        if (user != null) {
            val userName = user.displayName
            textView.text = "$userName"
        } else {
            // Handle the case where the user is not signed in
        }

        // Inside onCreateView() method

        val signOutButton = view.findViewById<Button>(R.id.btn_logout)
        textView6 = view.findViewById(R.id.textView6)
        textView3 = view.findViewById(R.id.textView3)
        textView2 = view.findViewById(R.id.textView2)
        imageView4 = view.findViewById(R.id.imageView4)

        signOutButton.setOnClickListener {
            signOutAndStartSignInActivity()
        }
        textView3.setOnClickListener {
            val intent = Intent(activity, ContactUs::class.java)
            startActivity(intent)
        }
        textView6.setOnClickListener {
            val intent = Intent(activity, EditProfile::class.java)
            startActivity(intent)
        }
        imageView4.setOnClickListener {
            val intent = Intent(activity, EditProfile::class.java)
            startActivity(intent)
        }
        textView2.setOnClickListener {
            val intent = Intent(activity, Pengaturan::class.java)
            startActivity(intent)
        }


        return view
    }

    private fun signOutAndStartSignInActivity() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Konfirmasi Keluar")
            .setMessage("Apakah Anda Yakin Ingin Keluar?")
            .setPositiveButton("Ya") { _, _ ->
                // Logout jika user memilih "Ya"
                mAuth.signOut()
                mGoogleSignInClient.signOut().addOnCompleteListener(requireActivity()) {
                    // Hapus data dari Shared Preferences
                    clearDataFromSharedPreferences()

                    // Pindah ke aktivitas login
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun clearDataFromSharedPreferences() {
        val sharedPreferences =
            requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

}
