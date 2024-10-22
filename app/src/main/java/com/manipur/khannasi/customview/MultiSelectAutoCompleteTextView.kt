package com.manipur.khannasi.customview

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.appcompat.app.AlertDialog

class MultiSelectAutoCompleteTextView(context: Context, attrs: AttributeSet) : AppCompatAutoCompleteTextView(context, attrs) {
    private val selectedItems = mutableListOf<String>()
    private lateinit var items: Array<String>

    init {
        setOnClickListener {
            showMultiSelectDialog()
        }
    }

    fun setItems(items: Array<String>) {
        this.items = items
    }

    override fun performClick(): Boolean {
        showMultiSelectDialog()
        return true
    }

    private fun showMultiSelectDialog() {
        val selected = BooleanArray(items.size) { index -> selectedItems.contains(items[index]) }
        val builder = AlertDialog.Builder(context)
        builder.setMultiChoiceItems(items, selected) { _, which, isChecked ->
            if (isChecked) {
                selectedItems.add(items[which])
            } else {
                selectedItems.remove(items[which])
            }
        }
        builder.setPositiveButton("OK") { dialog, _ ->
            setText(selectedItems.joinToString(", "))
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
}