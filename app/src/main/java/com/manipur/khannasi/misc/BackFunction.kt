package com.manipur.khannasi.misc

import android.content.Context
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

class BackFunction {
    companion object {
        fun onBackButtonClicked(fragment: Fragment) {
            fragment.fragmentManager?.popBackStack()
        }

        fun onBackPressed(fragment: Fragment) {
            val callback: OnBackPressedCallback =
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        fragment.fragmentManager?.popBackStack()
                    }
                }
            fragment.requireActivity().onBackPressedDispatcher.addCallback(fragment.viewLifecycleOwner, callback)
        }

        fun showExitConfirmationDialog(fragment: Fragment, context: Context, callback: (Boolean) -> Unit) {
            if (fragment.isAdded) {
                AlertDialog.Builder(context)
                    .setTitle("Confirm Exit")
                    .setMessage("Are you sure you want to leave this page? Your changes will not be saved.")
                    .setPositiveButton("Yes") { _, _ ->
                        callback(true)
                    }
                    .setNegativeButton("No") { _, _ ->
                        callback(false)
                    }
                    .show()
            } else {
                Log.e("BackFunction", "Fragment is not attached to an activity")
            }
        }
    }
}