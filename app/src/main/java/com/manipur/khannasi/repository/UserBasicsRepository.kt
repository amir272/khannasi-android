package com.manipur.khannasi.repository

import com.manipur.khannasi.dto.UserBasics
import com.manipur.khannasi.retrofit.ApiService
import com.manipur.khannasi.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserBasicsRepository {
    private val apiService: ApiService = RetrofitClient.instance

    fun getAllUserBasics(onResult: (List<UserBasics>?) -> Unit) {
        apiService.getAllUserBasics().enqueue(object : Callback<List<UserBasics>> {
            override fun onResponse(call: Call<List<UserBasics>>, response: Response<List<UserBasics>>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<List<UserBasics>>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun getUserBasicsById(id: Long, onResult: (UserBasics?) -> Unit) {
        apiService.getUserBasicsById(id).enqueue(object : Callback<UserBasics> {
            override fun onResponse(call: Call<UserBasics>, response: Response<UserBasics>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<UserBasics>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun getUserBasicsByUsername(username: String, onResult: (UserBasics?) -> Unit) {
        apiService.getUserBasicsByUsername(username).enqueue(object : Callback<UserBasics> {
            override fun onResponse(call: Call<UserBasics>, response: Response<UserBasics>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<UserBasics>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun postUserBasics(userBasics: UserBasics, onResult: (UserBasics?) -> Unit) {
        apiService.postUserBasics(userBasics).enqueue(object : Callback<UserBasics> {
            override fun onResponse(call: Call<UserBasics>, response: Response<UserBasics>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<UserBasics>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun putUserBasics(id: Long, userBasics: UserBasics, onResult: (UserBasics?) -> Unit) {
        apiService.putUserBasics(id, userBasics).enqueue(object : Callback<UserBasics> {
            override fun onResponse(call: Call<UserBasics>, response: Response<UserBasics>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<UserBasics>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun deleteUserBasics(id: Long, onResult: (Boolean) -> Unit) {
        apiService.deleteUserBasics(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onResult(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onResult(false)
            }
        })
    }
}