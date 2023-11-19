package com.rival.tutorialloginregist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class CoffeDetail : Fragment() {
private lateinit var btnLocation : Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_coffe_detail, container, false)

        // Ambil data yang dikirimkan dari ProfileFragment
        val image = arguments?.getInt("image", 0)
        val title = arguments?.getString("title")
        val ingredients = arguments?.getString("ingredients")
        val ButtonLokasi = view.findViewById<Button>(R.id.btnLocation)

        val imgView = view.findViewById<ImageView>(R.id.detailImage)
        val titleView = view.findViewById<TextView>(R.id.detailTitle)
        val ingredientsView = view.findViewById<TextView>(R.id.detailDesc)


        imgView.setImageResource(image ?: R.drawable.kopi1)
        titleView.text = title ?: "No Title" // Default title jika title tidak diterima
        ingredientsView.text = ingredients ?: "No Ingredients" // Default ingredients jika ingredients tidak diterima

        ButtonLokasi.setOnClickListener {
            val intent = Intent(activity, MapActivity::class.java)
            startActivity(intent)
        }
        return view
    }
}
