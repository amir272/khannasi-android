package com.manipur.khannasi.repository

import com.manipur.khannasi.dto.ArticleVote
import com.manipur.khannasi.retrofit.ApiService
import com.manipur.khannasi.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleVoteRepository {
    private val apiService: ApiService = RetrofitClient.instance

    fun getAllArticleVotes(onResult: (List<ArticleVote>?) -> Unit) {
        apiService.getAllArticleVotes().enqueue(object : Callback<List<ArticleVote>> {
            override fun onResponse(call: Call<List<ArticleVote>>, response: Response<List<ArticleVote>>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<List<ArticleVote>>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun getArticleVoteById(id: Long, onResult: (ArticleVote?) -> Unit) {
        apiService.getArticleVoteById(id).enqueue(object : Callback<ArticleVote> {
            override fun onResponse(call: Call<ArticleVote>, response: Response<ArticleVote>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<ArticleVote>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun postArticleVote(vote: ArticleVote, onResult: (ArticleVote?) -> Unit) {
        apiService.postArticleVote(vote).enqueue(object : Callback<ArticleVote> {
            override fun onResponse(call: Call<ArticleVote>, response: Response<ArticleVote>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<ArticleVote>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun putArticleVote(id: Long, vote: ArticleVote, onResult: (ArticleVote?) -> Unit) {
        apiService.putArticleVote(id, vote).enqueue(object : Callback<ArticleVote> {
            override fun onResponse(call: Call<ArticleVote>, response: Response<ArticleVote>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<ArticleVote>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun deleteArticleVote(id: Long, onResult: (Boolean) -> Unit) {
        apiService.deleteArticleVote(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onResult(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onResult(false)
            }
        })
    }
}