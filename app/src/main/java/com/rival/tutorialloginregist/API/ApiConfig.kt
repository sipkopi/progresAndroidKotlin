package com.rival.tutorialloginregist.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    const val baseUrl = "https://dtaapi.000webhostapp.com/dataapi/api.php"

    fun getRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}
    fun getService() : ApiService{
        return getRetrofit().create(ApiService::class.java)
    }
}