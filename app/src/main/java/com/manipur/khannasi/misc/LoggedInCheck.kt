package com.manipur.khannasi.misc

import android.content.Context
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE

class LoggedInCheck(private val context: Context) {

    fun isLoggedIn(): Boolean {
        val sharedPreferences = context.getSharedPreferences("UserPreferences", MODE_PRIVATE)
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }
}