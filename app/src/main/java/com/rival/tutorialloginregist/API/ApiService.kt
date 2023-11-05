package com.rival.tutorialloginregist.API

import com.rival.tutorialloginregist.ResponseKopi
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("character")
    fun getKopi() : Call<ResponseKopi>
}