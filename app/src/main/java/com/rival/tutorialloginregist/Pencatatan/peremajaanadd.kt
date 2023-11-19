package com.rival.tutorialloginregist.Pencatatan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.rival.tutorialloginregist.R

class peremajaanadd : AppCompatActivity() {
    private lateinit var  tabLayout : TabLayout
    private lateinit var viewPager2 : ViewPager2
    private lateinit var adapter: FragmentPageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_peremajaanadd)

        tabLayout = findViewById(R.id.tabLayout)
        viewPager2 = findViewById(R.id.viewPager)

        adapter = FragmentPageAdapter(supportFragmentManager,lifecycle)

        tabLayout.addTab(tabLayout.newTab().setText("Penyiraman"))
        tabLayout.addTab(tabLayout.newTab().setText("Pemupukan"))

        viewPager2.adapter = adapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager2.currentItem = tab?.position ?: 0
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
     viewPager2.registerOnPageChangeCallback(object  : ViewPager2.OnPageChangeCallback(){
         override fun onPageSelected(position: Int) {
             super.onPageSelected(position)
             tabLayout.selectTab(tabLayout.getTabAt(position))
         }
     })
    }
}