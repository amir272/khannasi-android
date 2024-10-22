package com.manipur.khannasi.retrofit

import com.manipur.khannasi.constants.CURRENT_ENV
import com.manipur.khannasi.util.CurrentUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val instance: ApiService by lazy {
        val retrofit = CurrentUrl.get(CURRENT_ENV)?.let {
            Retrofit.Builder()
                .baseUrl(it)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        retrofit?.create(ApiService::class.java) ?: throw Exception("Retrofit instance creation failed")
    }
}