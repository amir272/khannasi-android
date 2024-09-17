package com.manipur.khannasi.repository

import com.manipur.khannasi.dto.DiscussionVote
import com.manipur.khannasi.retrofit.ApiService
import com.manipur.khannasi.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiscussionVoteRepository {
    private val apiService: ApiService = RetrofitClient.instance

    fun getAllDiscussionVotes(onResult: (List<DiscussionVote>?) -> Unit) {
        apiService.getAllDiscussionVotes().enqueue(object : Callback<List<DiscussionVote>> {
            override fun onResponse(call: Call<List<DiscussionVote>>, response: Response<List<DiscussionVote>>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<List<DiscussionVote>>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun getDiscussionVoteById(id: Long, onResult: (DiscussionVote?) -> Unit) {
        apiService.getDiscussionVoteById(id).enqueue(object : Callback<DiscussionVote> {
            override fun onResponse(call: Call<DiscussionVote>, response: Response<DiscussionVote>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<DiscussionVote>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun postDiscussionVote(vote: DiscussionVote, onResult: (DiscussionVote?) -> Unit) {
        apiService.postDiscussionVote(vote).enqueue(object : Callback<DiscussionVote> {
            override fun onResponse(call: Call<DiscussionVote>, response: Response<DiscussionVote>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<DiscussionVote>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun putDiscussionVote(id: Long, vote: DiscussionVote, onResult: (DiscussionVote?) -> Unit) {
        apiService.putDiscussionVote(id, vote).enqueue(object : Callback<DiscussionVote> {
            override fun onResponse(call: Call<DiscussionVote>, response: Response<DiscussionVote>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<DiscussionVote>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun deleteDiscussionVote(id: Long, onResult: (Boolean) -> Unit) {
        apiService.deleteDiscussionVote(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onResult(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onResult(false)
            }
        })
    }
}