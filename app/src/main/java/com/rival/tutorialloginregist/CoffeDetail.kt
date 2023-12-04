package com.rival.tutorialloginregist

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

class CoffeDetail : Fragment() {
    private lateinit var btnLocation: Button
    private lateinit var copyText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_coffe_detail, container, false)

        copyText = view.findViewById(R.id.copyText)
        // Ambil data yang dikirimkan dari ProfileFragment
        val imageUrl = arguments?.getString("gambar1")
        val title = arguments?.getString("varietas_kopi")
        val ingredients = arguments?.getString("metode_pengolahan")
        val desk = arguments?.getString("deskripsi")
        val roasting = arguments?.getString("tgl_roasting")
        val panen = arguments?.getString("tgl_panen")
        val link = arguments?.getString("link")

        val ButtonLokasi = view.findViewById<Button>(R.id.btnLocation)

        val imgView = view.findViewById<ImageView>(R.id.detailImage)
        val titleView = view.findViewById<TextView>(R.id.detailTitle)
        val ingredientsView = view.findViewById<TextView>(R.id.detailDesc)
        val desKripsi = view.findViewById<TextView>(R.id.detailDeskripsi)
        val tglpanen = view.findViewById<TextView>(R.id.detailTglPanen)
        val tglroasting = view.findViewById<TextView>(R.id.detailRoasting)
        val Link = view.findViewById<TextView>(R.id.detailExpired)

        // Menggunakan Glide untuk memuat gambar dari URL ke ImageView
        if (!imageUrl.isNullOrBlank()) {
            Glide.with(requireContext())
                .load(imageUrl)
                .into(imgView)
        } else {
            imgView.setImageResource(R.drawable.kopi2) // Atur gambar default jika URL tidak tersedia
        }

        titleView.text = title ?: "No Title"
        desKripsi.text = desk ?: "kopi ini mantap" // Default title jika title tidak diterima
        ingredientsView.text = ingredients ?: "No Ingredients" // Default ingredients jika ingredients tidak diterima
        tglpanen.text = panen ?: "2023/12/3"
        tglroasting.text = roasting ?: "2023/12/3"
        Link.text = link ?: "https://sipkopi.com"

        // Set teks yang akan disalin ke clipboard
        val textToCopy = Link.text.toString()

        // Menambahkan fungsi onClickListener ke TextView untuk menyalin teks ke clipboard
        copyText.setOnClickListener {
            copyToClipboard(textToCopy)
        }

        ButtonLokasi.setOnClickListener {
            val intent = Intent(activity, MapActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    // Metode untuk menyalin teks ke clipboard
    private fun copyToClipboard(text: String) {
        val clipboard: ClipboardManager? =
            context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?

        val clip = ClipData.newPlainText("label", text)
        clipboard?.setPrimaryClip(clip)

        Toast.makeText(context, "Link Berhasil Disalin", Toast.LENGTH_SHORT).show()
    }
}
