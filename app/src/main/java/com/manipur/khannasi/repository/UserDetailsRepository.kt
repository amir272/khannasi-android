package com.manipur.khannasi.repository

import com.manipur.khannasi.dto.UserDetails
import com.manipur.khannasi.retrofit.ApiService
import com.manipur.khannasi.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailsRepository {
    private val apiService: ApiService = RetrofitClient.instance

    fun getAllUserDetails(onResult: (List<UserDetails>?) -> Unit) {
        apiService.getAllUserDetails().enqueue(object : Callback<List<UserDetails>> {
            override fun onResponse(call: Call<List<UserDetails>>, response: Response<List<UserDetails>>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<List<UserDetails>>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun getUserDetailsById(userId: Long, onResult: (UserDetails?) -> Unit) {
        apiService.getUserDetailsById(userId).enqueue(object : Callback<UserDetails?> {
            override fun onResponse(call: Call<UserDetails?>, response: Response<UserDetails?>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<UserDetails?>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun getUserDetailsByUsernameAndPassword(username: String, password: String, onResult: (UserDetails?) -> Unit) {
        apiService.getUserDetailsByUsernameAndPassword(username, password).enqueue(object : Callback<UserDetails?> {
            override fun onResponse(call: Call<UserDetails?>, response: Response<UserDetails?>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<UserDetails?>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun postUserDetails(userDetails: UserDetails, onResult: (UserDetails?) -> Unit) {
        apiService.postUserDetails(userDetails).enqueue(object : Callback<UserDetails> {
            override fun onResponse(call: Call<UserDetails>, response: Response<UserDetails>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<UserDetails>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun putUserDetails(userId: Long, userDetails: UserDetails, onResult: (UserDetails?) -> Unit) {
        apiService.putUserDetails(userId, userDetails).enqueue(object : Callback<UserDetails?> {
            override fun onResponse(call: Call<UserDetails?>, response: Response<UserDetails?>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<UserDetails?>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun deleteUserDetails(userId: Long, onResult: (Boolean) -> Unit) {
        apiService.deleteUserDetails(userId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onResult(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onResult(false)
            }
        })
    }
}