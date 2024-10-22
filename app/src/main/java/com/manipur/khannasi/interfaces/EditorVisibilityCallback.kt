package com.manipur.khannasi.interfaces

interface EditorVisibilityCallback {
    fun showEditor(replyTo: Long? = 0)
    fun hideEditor()
}