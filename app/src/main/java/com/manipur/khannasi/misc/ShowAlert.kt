package com.manipur.khannasi.misc

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.manipur.khannasi.R

class ShowAlert {
    companion object {
        fun showConfirmationAlert(context: Context, obj: Any?) {
            AlertDialog.Builder(context)
                .setTitle("Confirmation")
                .setMessage("$obj saved successfully.")
                .setPositiveButton("OK") { _, which ->
                    // Handle the OK button click event if needed
                }
                .setNeutralButton("No") { _, which ->
                    // Handle the No button click event if needed
                }
                .show()
        }

        fun showErrorAlert(context: Context, message: String) {
            AlertDialog.Builder(context)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK") { dialog, which ->
                    // Handle the OK button click event if needed
                }
                .show()
        }

        fun showToastSuccessAlert(context: Context, message: Any?) {
            Toast.makeText(context, "Successfully saved $message", Toast.LENGTH_LONG).show()
        }

        fun showToastErrorAlert(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        // ShowAlert.kt
        fun showToastAlertWithIcon(context: Context, message: Any?, success: Boolean?=true) {
            val inflater = LayoutInflater.from(context)
            val layout = inflater.inflate(R.layout.custom_toast, null)

            val toastIcon = layout.findViewById<ImageView>(R.id.toast_icon)
            val toastMessage = layout.findViewById<TextView>(R.id.toast_message)

            if(success == true) {
                toastIcon.setImageResource(R.drawable.check_mark)
                toastMessage.text = message.toString()
            } else {
                toastIcon.setImageResource(R.drawable.remove)
                toastMessage.text = message.toString()
            }

            val toast = Toast(context)
            toast.duration = Toast.LENGTH_LONG
            toast.view = layout
            toast.show()
        }
    }
}