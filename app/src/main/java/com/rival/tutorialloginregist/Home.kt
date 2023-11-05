package com.rival.tutorialloginregist


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import java.util.ArrayList
import androidx.fragment.app.FragmentTransaction

class Home : Fragment() {
    private lateinit var txt_Seeall : TextView
    private lateinit var usernameTextView: TextView
    private lateinit var adapter: RecyclerView.Adapter<*>
    private lateinit var recyclerViewList: RecyclerView
    private lateinit var textView9 : TextView
    private lateinit var textView7 : TextView
    private lateinit var imageView1 : ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Mengambil TextView dari layout
        usernameTextView = view.findViewById(R.id.txt_session)

        // Mengambil nama pengguna dari SessionManager
        val sessionManager = SessionManager(requireContext())
        val username = sessionManager.getUsername()

        textView9 = view.findViewById(R.id.textView9)
        textView7 = view.findViewById(R.id.textView7)
        imageView1 = view.findViewById(R.id.imageView1)

        // Tambahkan onClickListener untuk TextView
        textView9.setOnClickListener {
            val intent = Intent(activity, NotifikasiActivity::class.java)
            startActivity(intent)
        }
        imageView1.setOnClickListener {
            val intent = Intent(activity, WeatherActivity::class.java)
            startActivity(intent)
        }
        textView7.setOnClickListener {
            val intent = Intent(activity, MapActivity::class.java)
            startActivity(intent)
        }

        if (username != null) {
            // Menampilkan nama pengguna di TextView
            usernameTextView.text = "Selamat Datang, $username"
        }


        recyclerViewList = view.findViewById(R.id.view)
        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewList.layoutManager = linearLayoutManager
        val news = ArrayList<ListDomain>()
        news.add(ListDomain("Arabica Coffe", "kopi1"))
        news.add(ListDomain("Robusta Coffe", "kopi3"))
        news.add(ListDomain("Liberica Coffe", "kopi1"))
        news.add(ListDomain("Arabica Coffe", "kopi3"))

        adapter = NewsAdapter(news)
        recyclerViewList.adapter = adapter



        return view
    }

}
