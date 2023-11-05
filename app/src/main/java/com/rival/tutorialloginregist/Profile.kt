package com.rival.tutorialloginregist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rival.tutorialloginregist.coffe

class Profile : Fragment() {

    private lateinit var imageId: Array<Int>
    private lateinit var names: Array<String>
    private lateinit var ingredients: Array<String>

    private lateinit var recView: RecyclerView
    private lateinit var itemArrayList: ArrayList<coffe>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_recycler_view, container, false)

        imageId = arrayOf(
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
            "Arabica Coffee 1",
            "Robusta Coffee 2",
            "Liberica Coffee 3",
            "Arabica Coffee 4",
            "Robusta Coffee 5",
            "Liberica Coffee 6",
            "Arabica Coffee 7",
            "Robusta Coffee 8",
            "Liberica Coffee 9"
        )

        ingredients = arrayOf(
            "Blue Mountain 1, cheese, oregano",
            "Blue Mountain 2, cheese, oregano",
            "Blue Mountain 3, cheese, oregano",
            "Blue Mountain 4, cheese, oregano",
            "Blue Mountain 5, cheese, oregano",
            "Blue Mountain 6, cheese, oregano",
            "Blue Mountain 7, cheese, oregano",
            "Blue Mountain 8, cheese, oregano",
            "Blue Mountain 9, cheese, oregano"
        )

        recView = view.findViewById(R.id.recView)
        recView.layoutManager = GridLayoutManager(requireContext(), 2)
        recView.setHasFixedSize(true)

        itemArrayList = arrayListOf()

        getData()

        recView.adapter = RecAadapter(itemArrayList)

        return view
    }

    private fun getData() {
        for (i in imageId.indices) {
            if (i < names.size && i < ingredients.size) {
                val coffee = coffe(imageId[i], names[i], ingredients[i])
                itemArrayList.add(coffee)
            }
        }
    }
}
