package com.rival.tutorialloginregist.ProfilePage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.rival.tutorialloginregist.Pencatatan.FragmentPageAdapter
import com.rival.tutorialloginregist.R

class ContactUs : AppCompatActivity() {
    private lateinit var  tabLayout : TabLayout
    private lateinit var viewPager1 : ViewPager2
    private lateinit var adapter: FragmentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)


        tabLayout = findViewById(R.id.tabLayout1)
        viewPager1 = findViewById(R.id.viewPager1)

        adapter = FragmentAdapter(supportFragmentManager,lifecycle)

        tabLayout.addTab(tabLayout.newTab().setText("Hubungi Admin"))
        tabLayout.addTab(tabLayout.newTab().setText("Hubungi Ahli"))

        viewPager1.adapter = adapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager1.currentItem = tab?.position ?: 0
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        viewPager1.registerOnPageChangeCallback(object  : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }


}
