package com.rival.tutorialloginregist


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rival.tutorialloginregist.API.ApiConfig
import com.rival.tutorialloginregist.API.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val morty = findViewById<RecyclerView>(R.id.rv_morty)

        ApiConfig.getService().getKopi().enqueue(object : Callback<ResponseKopi>{
            override fun onResponse(call: Call<ResponseKopi>, response: Response<ResponseKopi>) {
                if (response.isSuccessful){
                    val responseMorty = response.body()
                    val dataMorty = responseMorty?.results
                    val mortyAdapter = KopiAdapter(dataMorty)
                    morty.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity2)
                        setHasFixedSize(true)
                        mortyAdapter.notifyDataSetChanged()
                        adapter = mortyAdapter
                    }
                }
            }

            override fun onFailure(call: Call<ResponseKopi>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

        })

    }
}