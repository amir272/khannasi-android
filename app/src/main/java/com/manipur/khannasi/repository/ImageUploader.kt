package com.manipur.khannasi.repository

import android.util.Log
import com.manipur.khannasi.retrofit.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

object ImageUploader {
    fun uploadImageToServer(imagePath: String, callback: (String?) -> Unit) {
        val file = File(imagePath)
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        val call = RetrofitClient.instance.uploadImage(body)
        call.enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                if (response.isSuccessful) {
                    val mapResponse = response.body()
                    val imagePathForServer = mapResponse?.get("location").toString()
                    Log.d("ImageUploader", "Response: $imagePathForServer")
                    callback(imagePathForServer)
                } else {
                    Log.e("ImageUploader", "Error: ${response.errorBody()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                Log.e("ImageUploader", "Failure: ${t.message}")
                callback(null)
            }
        })
    }
}