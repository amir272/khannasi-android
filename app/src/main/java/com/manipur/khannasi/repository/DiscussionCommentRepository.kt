package com.manipur.khannasi.repository

import com.manipur.khannasi.dto.DiscussionComment
import com.manipur.khannasi.retrofit.ApiService
import com.manipur.khannasi.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiscussionCommentRepository {
    private val apiService: ApiService = RetrofitClient.instance

    fun getAllDiscussionComments(onResult: (List<DiscussionComment>?) -> Unit) {
        apiService.getAllDiscussionComments().enqueue(object : Callback<List<DiscussionComment>> {
            override fun onResponse(call: Call<List<DiscussionComment>>, response: Response<List<DiscussionComment>>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<List<DiscussionComment>>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun getDiscussionCommentById(id: Long, onResult: (DiscussionComment?) -> Unit) {
        apiService.getDiscussionCommentById(id).enqueue(object : Callback<DiscussionComment> {
            override fun onResponse(call: Call<DiscussionComment>, response: Response<DiscussionComment>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<DiscussionComment>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun postDiscussionComment(comment: DiscussionComment, onResult: (DiscussionComment?) -> Unit) {
        apiService.postDiscussionComment(comment).enqueue(object : Callback<DiscussionComment> {
            override fun onResponse(call: Call<DiscussionComment>, response: Response<DiscussionComment>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<DiscussionComment>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun putDiscussionComment(id: Long, comment: DiscussionComment, onResult: (DiscussionComment?) -> Unit) {
        apiService.putDiscussionComment(id, comment).enqueue(object : Callback<DiscussionComment> {
            override fun onResponse(call: Call<DiscussionComment>, response: Response<DiscussionComment>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<DiscussionComment>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun deleteDiscussionComment(id: Long, onResult: (Boolean) -> Unit) {
        apiService.deleteDiscussionComment(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onResult(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onResult(false)
            }
        })
    }
}