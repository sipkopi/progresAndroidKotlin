package com.rival.tutorialloginregist.Pencatatan

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MyVolleyRequest(private val context: Context) {
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun postRequest(
        url: String,
        params: JSONObject,
        listener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener
    ) {
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url,
            params,
            listener,
            errorListener
        )
        requestQueue.add(jsonObjectRequest)
    }

    companion object {
        const val TAG = "MyVolleyRequest"
    }
}
