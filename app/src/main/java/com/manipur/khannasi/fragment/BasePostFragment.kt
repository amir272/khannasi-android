package com.manipur.khannasi.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.manipur.khannasi.R
import com.manipur.khannasi.customview.MultiSelectAutoCompleteTextView
import com.manipur.khannasi.repository.ImageUploader.uploadImageToServer
import com.manipur.khannasi.util.SaveImageToLocal.Companion.saveImageToLocal
import com.manipur.khannasi.util.SharedViewModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import jp.wasabeef.richeditor.RichEditor
import kotlinx.coroutines.launch
import java.io.File

abstract class BasePostFragment : Fragment() {

    protected lateinit var sharedViewModel: SharedViewModel
    protected lateinit var mEditor: RichEditor
    protected lateinit var actionBold: View
    protected lateinit var actionItalic: View
    protected lateinit var actionUnderline: View
    protected lateinit var actionInsertBullets: View
    protected lateinit var actionAddImage: ImageButton
    protected lateinit var savedImagePathTextView: TextView
    protected lateinit var titleInput: TextInputEditText
    protected lateinit var categoryInput: AutoCompleteTextView
    protected lateinit var subCategoryInput: MultiSelectAutoCompleteTextView
    protected lateinit var postButton: Button
    protected var imagePathForServer: String? = null
    protected var isActionBold = false
    protected var isActionItalic = false
    protected var isActionUnderline = false
    protected var isActionInsertBullets = false
    protected var imagePath: String? = null

    companion object {
        private const val REQUEST_CODE_SELECT_IMAGE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_editor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        actionBold = view.findViewById(R.id.action_bold)
        actionItalic = view.findViewById(R.id.action_italic)
        actionUnderline = view.findViewById(R.id.action_underline)
        actionInsertBullets = view.findViewById(R.id.action_insert_bullets)
        actionAddImage = view.findViewById(R.id.action_add_image)
        savedImagePathTextView = view.findViewById(R.id.imageSavedPath)
        titleInput = view.findViewById(R.id.title)
        categoryInput = view.findViewById(R.id.category)
        subCategoryInput = view.findViewById(R.id.sub_categories)
        postButton = view.findViewById(R.id.post)

        mEditor = view.findViewById(R.id.editor)
        mEditor.focusEditor()

        subCategoryInput.setItems(emptyArray())

        mEditor.setEditorHeight(100)
        mEditor.setEditorFontSize(22)
        mEditor.setEditorFontColor(1)
        mEditor.setPlaceholder("Insert text here...")
        mEditor.setPadding(10, 10, 10, 10)

        actionBold.setOnClickListener {
            isActionBold = !isActionBold
            setBackGround(actionBold, isActionBold)
            mEditor.setBold()
        }
        actionItalic.setOnClickListener {
            isActionItalic = !isActionItalic
            setBackGround(actionItalic, isActionItalic)
            mEditor.setItalic()
        }
        actionUnderline.setOnClickListener {
            isActionUnderline = !isActionUnderline
            setBackGround(actionUnderline, isActionUnderline)
            mEditor.setUnderline()
        }
        actionInsertBullets.setOnClickListener {
            isActionInsertBullets = !isActionInsertBullets
            mEditor.setBullets()
        }

        actionAddImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
        }

        postButton.setOnClickListener {
            postContent()
        }

        categoryInput.setOnItemClickListener { _, _, position, _ ->
            val selectedCategory = resources.getStringArray(R.array.categories)[position]
            val subCategories = getSubCategories(selectedCategory)
            subCategoryInput.setItems(subCategories)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            selectedImageUri?.let {
                imagePath = saveImageToLocal(it, requireContext())
                val actualImageFile = File(imagePath)

                lifecycleScope.launch {
                    val compressedImageFile = context?.let { it1 ->
                        Compressor.compress(it1, actualImageFile) {
                            quality(100)
                            format(Bitmap.CompressFormat.WEBP)
                            resolution(560, 320)
                            size(50000)
                        }
                    }!!
                    imagePath = compressedImageFile.absolutePath

                    if (imagePath != null) {
                        uploadImageToServer(imagePath!!) { savedImagePath ->
                            Log.d("BasePostFragment", "onActivityResult: $savedImagePath")
                            savedImagePath?.let {
                                savedImagePathTextView.text = "Uploaded successfully"
                                imagePathForServer = savedImagePath
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setBackGround(view: View, isAction: Boolean) {
        if (isAction) {
            view.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_border)
        } else {
            view.background = null
        }
    }

    protected abstract fun postContent()

    protected abstract fun getSubCategories(selectedCategory: String): Array<String>
}