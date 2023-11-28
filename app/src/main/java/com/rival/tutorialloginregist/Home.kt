package com.rival.tutorialloginregist

import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.rival.tutorialloginregist.laporan.laporanActivity
import java.util.ArrayList

class Home : Fragment() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    private lateinit var usernameTextView: TextView
    private lateinit var adapter: NewsAdapter
    private lateinit var recyclerViewList: RecyclerView
    private lateinit var textView9: TextView
    private lateinit var textView7: TextView
    private lateinit var imageView6: ImageView
    private lateinit var imageView1: ImageView
    private lateinit var imageView8: ImageView
    private lateinit var imageView4: ImageView
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
        val textSeeal: TextView = view.findViewById(R.id.txt_seeall)
        textSeeal.setOnClickListener {
            val fragment = Profile()
            val transaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, fragment).commit()

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
        imageView6 = view.findViewById(R.id.imageView6)
        imageView8 = view.findViewById(R.id.imageView8)
        imageView4 = view.findViewById(R.id.imageView4)
        textView9.setOnClickListener {
            val intent = Intent(activity, NotifikasiActivity::class.java)
            startActivity(intent)
        }
        imageView1.setOnClickListener {
            val intent = Intent(activity, NotifikasiActivity2::class.java)
            startActivity(intent)
        }
        imageView4.setOnClickListener {
            val intent = Intent(activity, laporanActivity::class.java)
            startActivity(intent)
        }
        textView7.setOnClickListener {
            val intent = Intent(activity, WeatherActivity::class.java)
            startActivity(intent)
        }
        imageView6.setOnClickListener {
            val intent = Intent(activity, WeatherActivity::class.java)
            startActivity(intent)
        }
        imageView8.setOnClickListener {
            val intent = Intent(activity, NotifikasiActivity::class.java)
            startActivity(intent)
        }

        // Menampilkan nama pengguna di TextView
        //  usernameTextView.text = userName

        recyclerViewList = view.findViewById(R.id.view)
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewList.layoutManager = linearLayoutManager

        val news = ArrayList<ListDomain>()
        news.add(
            ListDomain(
                "Teknik Terbaru dalam Budidaya Kopi...",
                "artikel2",
                "https://www.mertani.co.id/post/teknik-budidaya-kopi-yang-berkelanjutan"
            )
        )
        news.add(
            ListDomain(
                "Meningkatkan Kualitas Kopi Anda: Tips...",
                "artikel3",
                "https://menitinspirasi.biz.id/petani-kopi/"
            )
        )
        news.add(
            ListDomain(
                "Peran Kualitas Tanah dalam Pertumbuhan...",
                "artikel4",
                "https://www.researchgate.net/publication/275276092_Distribution_of_Soil_Fertility_of_Smallholding_Arabica_Coffee_Farms_at_Ijen-Raung_Highland_Areas_Based_on_Altitude_and_Shade_Trees/fulltext/5ba0fa9845851574f7d37b71/Distribution-of-Soil-Fertility-of-Smallholding-Arabica-Coffee-Farms-at-Ijen-Raung-Highland-Areas-Based-on-Altitude-and-Shade-Trees.pdf"
            )
        )
        news.add(
            ListDomain(
                "Ketahui Musim Terbaik untuk Menanam Kopi...",
                "artikel5",
                "https://coffeeland.co.id/benarkah-musim-hujan-mempengaruhi-panen-kopi/"
            )
        )

        adapter = NewsAdapter(news)
        recyclerViewList.adapter = adapter

        adapter.setOnItemClickListener(object : NewsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val clickedItem = news[position]
                val articleUrl = clickedItem.link

                if (!articleUrl.isNullOrBlank()) { // Menggunakan isNullOrBlank untuk menangani URL yang kosong atau hanya berisi spasi
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl))

                    // Memeriksa apakah ada aktivitas yang dapat menangani intent
                    if (intent.resolveActivity(requireActivity().packageManager) != null) {
                        startActivity(intent)
                    } else {
                        Toast.makeText(requireContext(), "Tidak ada aplikasi yang dapat menangani tautan", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Tautan artikel tidak valid", Toast.LENGTH_SHORT).show()
                }
            }
        })

        return view
    }
}
