package com.manipur.khannasi.util

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPreferencesRetriever {
    val gson = Gson()

    inline fun <reified T> getDetails(context: Context, key: String): T? {
        val sharedPreferences = context.getSharedPreferences("UserPreferences", AppCompatActivity.MODE_PRIVATE)
        val detailsJson = sharedPreferences.getString(key, null)
        return if (detailsJson != null) {
            val type = TypeToken.getParameterized(T::class.java).type
            gson.fromJson(detailsJson, type)
        } else {
            null
        }
    }
}
