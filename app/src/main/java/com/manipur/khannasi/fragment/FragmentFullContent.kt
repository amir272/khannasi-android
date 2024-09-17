package com.manipur.khannasi.fragment

import android.os.Bundle
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.manipur.khannasi.R
import com.manipur.khannasi.misc.BackFunction.Companion.onBackButtonClicked
import com.manipur.khannasi.misc.LoadingSpinner
import com.manipur.khannasi.misc.SpannedHtmlString.Companion.fromHtml

class FragmentFullContent: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_full_page, container, false)
        val tvOutput = view.findViewById<TextView>(R.id.full_content_text)
        val content = arguments?.getString("content")
        Log.d("FragmentFullContent", "Content: $content")
        val htmlContent = content ?: "An error happened. Please try again later."
        val spanned: Spanned = fromHtml(htmlContent, requireContext(), tvOutput, 600, 900)
        tvOutput.text = spanned
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LoadingSpinner.hideLoadingSpinner(this, requireContext())

        val toolbar = view.findViewById<Toolbar>(R.id.full_content_toolbar)
        toolbar.setNavigationIcon(R.drawable.back_button_vector) // Set your own back arrow icon

        toolbar.setNavigationOnClickListener {
            LoadingSpinner.showLoadingSpinner(this, requireContext())
            onBackButtonClicked(this)
        }
    }
}