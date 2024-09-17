package com.manipur.khannasi.repository

import com.manipur.khannasi.dto.Discussion
import com.manipur.khannasi.retrofit.ApiService
import com.manipur.khannasi.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiscussionRepository {
    private val apiService: ApiService = RetrofitClient.instance

    fun getAllDiscussions(onResult: (List<Discussion>?) -> Unit) {
        apiService.getAllDiscussions().enqueue(object : Callback<List<Discussion>> {
            override fun onResponse(call: Call<List<Discussion>>, response: Response<List<Discussion>>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<List<Discussion>>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun getDiscussionById(id: Long, onResult: (Discussion?) -> Unit) {
        apiService.getDiscussionById(id).enqueue(object : Callback<Discussion> {
            override fun onResponse(call: Call<Discussion>, response: Response<Discussion>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<Discussion>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun postDiscussion(discussion: Discussion, onResult: (Discussion?) -> Unit) {
        apiService.postDiscussion(discussion).enqueue(object : Callback<Discussion> {
            override fun onResponse(call: Call<Discussion>, response: Response<Discussion>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<Discussion>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun putDiscussion(id: Long, discussion: Discussion, onResult: (Discussion?) -> Unit) {
        apiService.putDiscussion(id, discussion).enqueue(object : Callback<Discussion> {
            override fun onResponse(call: Call<Discussion>, response: Response<Discussion>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<Discussion>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun deleteDiscussion(id: Long, onResult: (Boolean) -> Unit) {
        apiService.deleteDiscussion(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onResult(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onResult(false)
            }
        })
    }
}