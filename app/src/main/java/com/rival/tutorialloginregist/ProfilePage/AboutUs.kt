package com.rival.tutorialloginregist.ProfilePage

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rival.tutorialloginregist.databinding.ActivityAboutUsBinding

class AboutUs : AppCompatActivity() {
    private lateinit var binding : ActivityAboutUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.LinkButton.setOnClickListener{
            openUrl("https://dtaapi.000webhostapp.com/detail/?kode_kopi=KPK001")
        }
    }

    private fun openUrl(link: String) {
        val uri = Uri.parse(link)
        val inte = Intent(Intent.ACTION_VIEW, uri)

        startActivity(inte)
    }
}