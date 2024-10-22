package com.manipur.khannasi.util

import android.util.Log
import android.widget.EditText

class CheckEditTextValue {
    companion object {
        fun checkIfNullOrEmpty(editText: EditText): Boolean {
            Log.d(editText.toString(), editText.text.toString())
            if (editText.text == null || editText.text.toString().isBlank()) {
                editText.error = "Required field"
                return true
            } else {
                return false
            }
        }
    }
}