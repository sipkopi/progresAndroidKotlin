package com.rival.tutorialloginregist

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
            openUrl("https://instagram.com/nmfadlii")
        }
    }

    private fun openUrl(link: String) {
        val uri = Uri.parse(link)
        val inte = Intent(Intent.ACTION_VIEW, uri)

        startActivity(inte)
    }
}