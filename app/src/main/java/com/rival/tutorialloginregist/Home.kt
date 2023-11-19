package com.rival.tutorialloginregist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import java.util.ArrayList

class Home : Fragment() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    private lateinit var usernameTextView: TextView
    private lateinit var adapter: RecyclerView.Adapter<*>
    private lateinit var recyclerViewList: RecyclerView
    private lateinit var textView9: TextView
    private lateinit var textView7: TextView
    private lateinit var imageView1: ImageView
    private lateinit var viewPager: ViewPager

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
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val textSeeal : TextView = view.findViewById(R.id.txt_seeall)
        textSeeal.setOnClickListener{
            val fragment = Profile()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_layout,fragment)?.commit()

        }

        // Mengambil TextView dari layout
        usernameTextView = view.findViewById(R.id.txt_session)

        // Mengambil nama pengguna dari Firebase
        val user = mAuth.currentUser
        if (user != null) {
            val userName = user.displayName
            if (userName != null) {
                usernameTextView.text = userName
            } else {
                // Jika user memiliki akun Google tetapi displayName-nya null
                usernameTextView.text = "Admin"
            }
        } else {
            // Jika user belum login
            usernameTextView.text = "Admin"
        }
        textView9 = view.findViewById(R.id.textView9)
        textView7 = view.findViewById(R.id.textView7)
        imageView1 = view.findViewById(R.id.imageView1)
        textView9.setOnClickListener {
            val intent = Intent(activity, NotifikasiActivity::class.java)
            startActivity(intent)
        }
        imageView1.setOnClickListener {
            val intent = Intent(activity, NotifikasiActivity2::class.java)
            startActivity(intent)
        }
        textView7.setOnClickListener {
            val intent = Intent(activity, WeatherActivity::class.java)
            startActivity(intent)
        }

        // Menampilkan nama pengguna di TextView
      //  usernameTextView.text = userName

        recyclerViewList = view.findViewById(R.id.view)
        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewList.layoutManager = linearLayoutManager
        val news = ArrayList<ListDomain>()
        news.add(ListDomain("Arabica Coffee", "kopi1"))
        news.add(ListDomain("Robusta Coffee", "kopi3"))
        news.add(ListDomain("Liberica Coffee", "kopi1"))
        news.add(ListDomain("Arabica Coffee", "kopi3"))

        adapter = NewsAdapter(news)
        recyclerViewList.adapter = adapter

        return view
    }
}
