// File: `src/main/java/com/manipur/khannasi/repository/DiscussionBookmarkRepository.kt`
package com.manipur.khannasi.repository

import com.manipur.khannasi.dto.DiscussionBookmark
import com.manipur.khannasi.retrofit.ApiService
import com.manipur.khannasi.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiscussionBookmarkRepository {
    private val apiService: ApiService = RetrofitClient.instance

    fun bookmarkDiscussion(bookmark: DiscussionBookmark, onResult: (DiscussionBookmark?) -> Unit) {
        apiService.bookmarkDiscussion(bookmark).enqueue(object : Callback<DiscussionBookmark> {
            override fun onResponse(call: Call<DiscussionBookmark>, response: Response<DiscussionBookmark>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<DiscussionBookmark>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun removeBookmark(discussionId: Long, userId: Long, onResult: (Boolean) -> Unit) {
        apiService.removeDiscussionBookmark(discussionId, userId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onResult(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onResult(false)
            }
        })
    }
}