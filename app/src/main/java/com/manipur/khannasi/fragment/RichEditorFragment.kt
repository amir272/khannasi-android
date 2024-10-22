package com.manipur.khannasi.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.manipur.khannasi.R
import com.manipur.khannasi.interfaces.EditorVisibilityCallback
import com.manipur.khannasi.interfaces.PostArticleWithEditor
import com.manipur.khannasi.interfaces.PostDiscussionWithEditor
import jp.wasabeef.richeditor.RichEditor

class RichEditorFragment(private val editorHeight: Int) : Fragment() {

    var editorVisibilityCallback: EditorVisibilityCallback? = null
    var postArticleWithEditor: PostArticleWithEditor? = null
    var postDiscussionWithEditor: PostDiscussionWithEditor? = null

    private lateinit var mEditor: RichEditor
    private lateinit var actionBold: ImageButton
    private lateinit var actionItalic: ImageButton
    private lateinit var actionUnderline: ImageButton
    private lateinit var actionInsertBullets: ImageButton
    private lateinit var postButton: Button
    private lateinit var cancelButton: Button

    lateinit var editorLayout: LinearLayout

    private var isActionBold = false
    private var isActionItalic = false
    private var isActionUnderline = false
    private var isActionInsertBullets = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.editor_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editorLayout = view.findViewById(R.id.editor_layout)
        mEditor = view.findViewById(R.id.editor)
        mEditor.focusEditor()
        actionBold = view.findViewById(R.id.action_bold)
        actionItalic = view.findViewById(R.id.action_italic)
        actionUnderline = view.findViewById(R.id.action_underline)
        actionInsertBullets = view.findViewById(R.id.action_insert_bullets)
        postButton = view.findViewById(R.id.post_button)
        cancelButton = view.findViewById(R.id.cancel_button)

        setupEditor()
        setupButtons()
    }

    private fun setupEditor() {
        mEditor.setEditorHeight(editorHeight)
        mEditor.setEditorFontSize(22)
        mEditor.setEditorFontColor(ContextCompat.getColor(requireContext(), R.color.black))
        mEditor.setPlaceholder("Insert text here...")
        mEditor.setPadding(10, 10, 10, 10)
    }

    private fun setupButtons() {
        actionBold.setOnClickListener {
            isActionBold = !isActionBold
            setBackground(actionBold, isActionBold)
            mEditor.setBold()
        }
        actionItalic.setOnClickListener {
            isActionItalic = !isActionItalic
            setBackground(actionItalic, isActionItalic)
            mEditor.setItalic()
        }
        actionUnderline.setOnClickListener {
            isActionUnderline = !isActionUnderline
            setBackground(actionUnderline, isActionUnderline)
            mEditor.setUnderline()
        }
        actionInsertBullets.setOnClickListener {
            isActionInsertBullets = !isActionInsertBullets
            setBackground(actionInsertBullets, isActionInsertBullets)
            mEditor.setBullets()
        }

        postButton.setOnClickListener {
            editorVisibilityCallback?.hideEditor()
            postArticleWithEditor?.postArticleComment()
            postDiscussionWithEditor?.postDiscussionComment()
        }
        cancelButton.setOnClickListener {
            editorVisibilityCallback?.hideEditor()
        }
    }

    private fun setBackground(view: View, isActive: Boolean) {
        if (isActive) {
            view.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_border)
        } else {
            view.background = null
        }
    }

    fun setContent(html: String) {
        mEditor.html = html
    }

    fun getContent(): String {
        return mEditor.html ?: ""
    }
}