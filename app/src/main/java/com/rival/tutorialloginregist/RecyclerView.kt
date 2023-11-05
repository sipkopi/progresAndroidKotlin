package com.rival.tutorialloginregist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rival.tutorialloginregist.coffe

class RecyclerView : AppCompatActivity() {

    private lateinit var imageId: Array<Int>
    private lateinit var names: Array<String>
    private lateinit var ingredients: Array<String>

    private lateinit var recView: RecyclerView
    private lateinit var itemArrayList: ArrayList<coffe>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        imageId = arrayOf(
            R.drawable.kopi1,
            R.drawable.kopi2,
            R.drawable.kopi3,
            R.drawable.kopi1,
            R.drawable.kopi2,
            R.drawable.kopi3,
            R.drawable.kopi1,
            R.drawable.kopi2,
            R.drawable.kopi3,
            R.drawable.kopi1,
            R.drawable.kopi2,
            R.drawable.kopi3
        )

        names = arrayOf(
            "Arabica Coffe",
            "Robusta Coffe",
            "Liberica Coffe"
        )

        ingredients = arrayOf(
            "Blue Mountain 1, cheese, oregano",
            "Blue Mountain 2, cheese, oregano",
            "Blue Mountain 3, cheese, oregano"
        )


        recView = findViewById(R.id.recView)
        // the type of the recyclerView. linear or grid
        recView.layoutManager = GridLayoutManager(this,3)

        recView.setHasFixedSize(true)


        itemArrayList = arrayListOf()

        getData()

        recView.adapter = RecAadapter(itemArrayList)


    }

    private fun getData() {

        for (i in imageId.indices) {
            val coffe = coffe(imageId[i], names[i], ingredients[i])
            itemArrayList.add(coffe)
        }
    }

}