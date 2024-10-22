package com.manipur.khannasi.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.manipur.khannasi.R
import com.manipur.khannasi.constants.CURRENT_ENV
import com.manipur.khannasi.util.CurrentUrl
import com.manipur.khannasi.util.DisplayImage

class ImageViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageView: ImageView = view.findViewById(R.id.full_image_view)
        val imageUrl = arguments?.getString("image_url")
        Log.d("ImageViewFragment", "Image URL: $imageUrl")

        imageUrl?.let {
            DisplayImage.loadImageFromUrl(requireContext(), imageView, CurrentUrl.get(CURRENT_ENV) + it)
        }

        val backButton = view.findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    companion object {
        fun newInstance(imageUrl: String): ImageViewFragment {
            val fragment = ImageViewFragment()
            val args = Bundle()
            args.putString("image_url", imageUrl)
            fragment.arguments = args
            return fragment
        }
    }
}