package com.blue_red.bensinpriser.api

import com.blue_red.bensinpriser.model.BDatum
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/.json/")
    fun retrieveAll(): Call<List<BDatum>>
}