package com.manipur.khannasi.misc

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class SaveImageToLocal {

    companion object {
        fun saveImageToLocal(uri: Uri, context: Context): String? {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val file = File(context.filesDir, "article_image.jpg")
            try {
                val outputStream = FileOutputStream(file)
                inputStream?.copyTo(outputStream)
                outputStream.close()
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
            return file.absolutePath
        }
    }
}