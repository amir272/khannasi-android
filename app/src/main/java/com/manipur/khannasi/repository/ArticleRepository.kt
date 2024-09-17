package com.manipur.khannasi.repository

import android.util.Log
import com.manipur.khannasi.dto.Article
import com.manipur.khannasi.retrofit.ApiService
import com.manipur.khannasi.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleRepository {
    private val apiService: ApiService = RetrofitClient.instance

    fun getAllArticles(onResult: (List<Article>?) -> Unit) {
        apiService.getAllArticles().enqueue(object : Callback<List<Article>> {
            override fun onResponse(call: Call<List<Article>>, response: Response<List<Article>>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<List<Article>>, t: Throwable) {
                Log.e("ArticleRepository", "Error: ${t.message}")
                onResult(null)
            }
        })
    }

    fun getArticleById(id: Long, onResult: (Article?) -> Unit) {
        apiService.getArticleById(id).enqueue(object : Callback<Article> {
            override fun onResponse(call: Call<Article>, response: Response<Article>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<Article>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun postArticle(article: Article, onResult: (Article?) -> Unit) {
        apiService.postArticle(article).enqueue(object : Callback<Article> {
            override fun onResponse(call: Call<Article>, response: Response<Article>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<Article>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun putArticle(id: Long, article: Article, onResult: (Article?) -> Unit) {
        apiService.putArticle(id, article).enqueue(object : Callback<Article> {
            override fun onResponse(call: Call<Article>, response: Response<Article>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<Article>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun deleteArticle(id: Long, onResult: (Boolean) -> Unit) {
        apiService.deleteArticle(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onResult(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onResult(false)
            }
        })
    }
}