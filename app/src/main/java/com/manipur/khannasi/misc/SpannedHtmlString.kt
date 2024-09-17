package com.manipur.khannasi.misc

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Spanned
import android.util.Log
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class SpannedHtmlString {
    companion object {
        fun fromHtml(htmlContent: String, context: Context, textView: TextView, desiredHeight: Int, desiredWidth: Int): Spanned {
            return HtmlCompat.fromHtml(htmlContent, HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM,
                { source ->
                    val drawable = BitmapDrawable()
                    Glide.with(context)
                        .asBitmap()
                        .load(source)
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                drawable.bitmap = resource
                                drawable.setBounds(0, 0, desiredWidth, desiredHeight)
//                                drawable.setBounds(0, 0, resource.width, resource.height)
                                textView.invalidate()
                                textView.text = textView.text // refresh text
                            }
                            override fun onLoadCleared(placeholder: Drawable?) {

                            }
                        })
                    drawable
                }, null
            )
        }
    }
}