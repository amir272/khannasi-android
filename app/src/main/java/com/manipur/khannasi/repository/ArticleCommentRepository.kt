package com.manipur.khannasi.repository

import com.manipur.khannasi.dto.ArticleComment
import com.manipur.khannasi.retrofit.ApiService
import com.manipur.khannasi.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleCommentRepository {
    private val apiService: ApiService = RetrofitClient.instance

    fun getAllArticleComments(onResult: (List<ArticleComment>?) -> Unit) {
        apiService.getAllArticleComments().enqueue(object : Callback<List<ArticleComment>> {
            override fun onResponse(call: Call<List<ArticleComment>>, response: Response<List<ArticleComment>>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<List<ArticleComment>>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun getArticleCommentById(id: Long, onResult: (ArticleComment?) -> Unit) {
        apiService.getArticleCommentById(id).enqueue(object : Callback<ArticleComment> {
            override fun onResponse(call: Call<ArticleComment>, response: Response<ArticleComment>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<ArticleComment>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun postArticleComment(comment: ArticleComment, onResult: (ArticleComment?) -> Unit) {
        apiService.postArticleComment(comment).enqueue(object : Callback<ArticleComment> {
            override fun onResponse(call: Call<ArticleComment>, response: Response<ArticleComment>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<ArticleComment>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun putArticleComment(id: Long, comment: ArticleComment, onResult: (ArticleComment?) -> Unit) {
        apiService.putArticleComment(id, comment).enqueue(object : Callback<ArticleComment> {
            override fun onResponse(call: Call<ArticleComment>, response: Response<ArticleComment>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<ArticleComment>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun deleteArticleComment(id: Long, onResult: (Boolean) -> Unit) {
        apiService.deleteArticleComment(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onResult(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onResult(false)
            }
        })
    }
}