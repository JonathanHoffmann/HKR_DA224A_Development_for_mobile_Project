package com.blue_red.bensinpriser.api

import com.blue_red.bensinpriser.model.BDatum
import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiRetriever  {

    private val service: ApiService

    companion object {
        const val BASE_URL = "https://bensinpriser-c8bc5.firebaseio.com"
    }

    init {

//        val gson = GsonBuilder()
//            .setLenient()
//            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(ApiService::class.java)
    }

    fun getData(callback: Callback<List<BDatum>>) {
        service.retrieveAll().enqueue(callback)
    }

}