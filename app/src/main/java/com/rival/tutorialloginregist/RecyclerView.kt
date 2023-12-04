package com.rival.tutorialloginregist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rival.tutorialloginregist.coffe

class RecyclerView : AppCompatActivity() {
    
    private lateinit var recView: RecyclerView
    private lateinit var itemArrayList: ArrayList<coffe>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)


        recView = findViewById(R.id.recView)
        // the type of the recyclerView. linear or grid
        recView.layoutManager = GridLayoutManager(this,3)

        recView.setHasFixedSize(true)


        itemArrayList = arrayListOf()


        recView.adapter = RecAadapter(itemArrayList)


    }



}