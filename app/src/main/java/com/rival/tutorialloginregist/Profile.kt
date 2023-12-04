package com.rival.tutorialloginregist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONArray
import org.json.JSONException

class Profile : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var recView: RecyclerView
    private lateinit var itemArrayList: ArrayList<coffe>
    private lateinit var adapter: RecAadapter
    private lateinit var dataQueue: RequestQueue


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataQueue = Volley.newRequestQueue(requireContext())
        itemArrayList = arrayListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.activity_recycler_view, container, false)
        fetchData()
        initializeUI(view)
        return view
    }

    private fun initializeUI(view: View) {
        searchView = view.findViewById(R.id.searchView)
        searchView.clearFocus()

        recView = view.findViewById(R.id.recView)
        recView.layoutManager = GridLayoutManager(requireContext(), 2)
        recView.setHasFixedSize(true)

        adapter = RecAadapter(itemArrayList)
        recView.adapter = adapter

        adapter.setOnItemClickListener { currentItem ->
            showCoffeeDetailFragment(currentItem)
      }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun showCoffeeDetailFragment(currentItem: coffe) {
        val fragmentKopiDetail = CoffeDetail()
        val bundle = Bundle().apply {
            putString("gambar1", currentItem.gambar1)
            putString("varietas_kopi", currentItem.varietasKopi)
            putString("metode_pengolahan", currentItem.metodePengolahan)
            putString("tgl_panen",currentItem.tglPanen)
            putString("tgl_roasting",currentItem.tglRoasting)
            putString("deskripsi",currentItem.Deskripsi)
            putString("link",currentItem.Link)

        }
        fragmentKopiDetail.arguments = bundle

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragmentKopiDetail)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun fetchData() {
        val url = "https://sipkopi.com/api/kopi/kopi.php"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                Log.d("Profile", "Response: $response")
                Log.d("Profile", "Number of items received: ${response.length()}")
                try {
                    requireActivity().runOnUiThread {
                        parseJson(response)
                    }
                } catch (e: JSONException) {
                    handleJsonParsingError(e)
                }
            },
            Response.ErrorListener { error ->
                handleVolleyError(error)
            }
        )

        // Tambahkan permintaan ke antrian
        dataQueue.add(jsonArrayRequest)
    }

    private fun parseJson(response: JSONArray) {
        for (i in 0 until response.length()) {
            val dataJson = response.getJSONObject(i)
            val coffee = coffe(
                dataJson.getString("varietas_kopi"),
                dataJson.getString("metode_pengolahan"),
                dataJson.getString("gambar1"),
                dataJson.getString("tgl_panen"),
                dataJson.getString("tgl_roasting"),
                dataJson.getString("deskripsi"),
                dataJson.getString("link")

            )
            itemArrayList.add(coffee)
        }
        adapter.notifyDataSetChanged()
        Log.d("Profile", "Number of items after parsing: ${itemArrayList.size}")
    }

    private fun handleJsonParsingError(e: JSONException) {
        e.printStackTrace()
        Log.e("Profile", "Error parsing JSON: ${e.message}")
    }

    private fun handleVolleyError(error: VolleyError) {
        error.printStackTrace()
        Log.e("Profile", "Volley error: ${error.message}")
    }
}

