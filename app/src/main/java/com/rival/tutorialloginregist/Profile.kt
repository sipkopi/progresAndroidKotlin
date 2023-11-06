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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi properti imageId, names, dan ingredients
        imageId = arrayOf(
            R.drawable.kopi1,
            R.drawable.kopi2,
            R.drawable.kopi3,
            R.drawable.kopi1
        )

        names = arrayOf(
            "Arabica Coffee",
            "Robusta Coffee",
            "Liberica Coffee",
            "Arabica Coffe"
        )

        ingredients = arrayOf(
            "Blue Mountain",
            "Natural",
            "Full Wash",
            "Orange Bourboun"
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_recycler_view, container, false)

        recView = view.findViewById(R.id.recView)
        recView.layoutManager = GridLayoutManager(requireContext(), 2)
        recView.setHasFixedSize(true)

        itemArrayList = arrayListOf()

        getData()

        val adapter = RecAadapter(itemArrayList)

        recView.adapter = adapter

        // Mengatur item click listener di adapter
        adapter.setOnItemClickListener { currentItem ->
            val fragmentKopiDetail = CoffeDetail()

            val bundle = Bundle()
            bundle.putInt("image", currentItem.imageTitle)
            bundle.putString("title", currentItem.name)
            bundle.putString("ingredients", currentItem.ingredients)
            fragmentKopiDetail.arguments = bundle

            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, fragmentKopiDetail)
            transaction.addToBackStack(null)
            transaction.commit()
        }

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
