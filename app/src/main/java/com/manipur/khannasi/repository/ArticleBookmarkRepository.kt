// File: `src/main/java/com/manipur/khannasi/repository/ArticleBookmarkRepository.kt`
package com.manipur.khannasi.repository

import com.manipur.khannasi.dto.ArticleBookmark
import com.manipur.khannasi.retrofit.ApiService
import com.manipur.khannasi.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleBookmarkRepository {
    private val apiService: ApiService = RetrofitClient.instance

    fun bookmarkArticle(bookmark: ArticleBookmark, onResult: (ArticleBookmark?) -> Unit) {
        apiService.bookmarkArticle(bookmark).enqueue(object : Callback<ArticleBookmark> {
            override fun onResponse(call: Call<ArticleBookmark>, response: Response<ArticleBookmark>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<ArticleBookmark>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun removeBookmark(articleId: Long, userId: Long, onResult: (Boolean) -> Unit) {
        apiService.removeBookmark(articleId, userId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onResult(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onResult(false)
            }
        })
    }
}