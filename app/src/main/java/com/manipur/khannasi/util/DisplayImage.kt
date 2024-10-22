package com.manipur.khannasi.util

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.squareup.picasso.Picasso

class DisplayImage {

    companion object {
        fun displayImageFromPath(imagePath: String, imageView: ImageView) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            imageView.setImageBitmap(bitmap)
        }

        fun getDrawableFromPath(context: Context, imagePath: String): Drawable {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            return BitmapDrawable(context.resources, bitmap)
        }

        fun loadImageFromUrl(context: Context, imageView: ImageView, url: String) {
            Picasso.get()
                .load(url)
                .into(imageView)
        }

    }
}