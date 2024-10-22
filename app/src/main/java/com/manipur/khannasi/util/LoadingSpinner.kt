package com.manipur.khannasi.util

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.manipur.khannasi.R

class LoadingSpinner {
    companion object {
        fun showLoadingSpinner(fragment: Fragment, context: Context) {
            val loadingSpinner = LayoutInflater.from(context).inflate(R.layout.loading_spinner, null)
            (fragment.requireActivity().window.decorView as ViewGroup).addView(loadingSpinner)
        }

        fun hideLoadingSpinner(fragment: Fragment, context: Context) {
            val decorView = fragment.requireActivity().window.decorView as ViewGroup
            val loadingSpinner = decorView.findViewById<FrameLayout>(R.id.loading_spinner)
            if (loadingSpinner != null) {
                decorView.removeView(loadingSpinner)
            }
        }
    }
}