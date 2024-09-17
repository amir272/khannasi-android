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
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.Toast
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.manipur.khannasi.R
import com.manipur.khannasi.dto.Article
import com.manipur.khannasi.dto.UserBasics
import com.manipur.khannasi.dto.UserDetails
import com.manipur.khannasi.misc.DisplayImage
import com.manipur.khannasi.misc.FtpClientUpload
import com.manipur.khannasi.misc.RetrieveDetailsFromSharedPreferences
import com.manipur.khannasi.misc.SaveImageToLocal.Companion.saveImageToLocal
import com.manipur.khannasi.misc.SharedViewModel
import com.manipur.khannasi.repository.ArticleRepository
import com.manipur.khannasi.retrofit.RetrofitClient
import com.manipur.khannasi.util.MultiSelectAutoCompleteTextView
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import jp.wasabeef.richeditor.RichEditor
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.InputStream

class EditorFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var mEditor: RichEditor
    private lateinit var mPreview: TextView
    private lateinit var editingOptions: HorizontalScrollView
    private lateinit var actionBold: View
    private lateinit var actionItalic: View
    private lateinit var actionUnderline: View
    private lateinit var actionInsertBullets: View
    private lateinit var actionAddImage: ImageButton
    private lateinit var previewImage: ImageView
    private lateinit var titleInput: TextInputEditText
    private lateinit var categoryInput: AutoCompleteTextView
    private lateinit var subCategoryInput: MultiSelectAutoCompleteTextView
    private lateinit var postButton: Button
    private var imagePathForServer: String? = null
    private var isActionBold = false
    private var isActionItalic = false
    private var isActionUnderline = false
    private var isActionInsertBullets = false
    private var isEditingOptionsVisible = false
    private var imagePath: String? = null

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

        editingOptions = view.findViewById(R.id.editingOptions)
        actionBold = view.findViewById(R.id.action_bold)
        actionItalic = view.findViewById(R.id.action_italic)
        actionUnderline = view.findViewById(R.id.action_underline)
        actionInsertBullets = view.findViewById(R.id.action_insert_bullets)
        actionAddImage = view.findViewById(R.id.action_add_image)
        previewImage = view.findViewById(R.id.previewImage)
        titleInput = view.findViewById(R.id.title)
        categoryInput = view.findViewById(R.id.category)
        subCategoryInput = view.findViewById(R.id.sub_categories)
        postButton = view.findViewById(R.id.post)

        mEditor = view.findViewById(R.id.editor)
        subCategoryInput.setItems(emptyArray())


        mEditor.setEditorHeight(200)
        mEditor.setEditorFontSize(22)
        mEditor.setEditorFontColor(1)
        mEditor.setPlaceholder("Insert text here...")
        mEditor.setPadding(10, 10, 10, 10)

        mPreview = view.findViewById(R.id.preview)
        mEditor.setOnTextChangeListener { text ->
            if (!isEditingOptionsVisible) {
                editingOptions.visibility = View.VISIBLE
                isEditingOptionsVisible = true
            }
            mPreview.text = text
        }

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
            postArticle()
        }

        categoryInput.setOnItemClickListener { _, _, position, _ ->
            val selectedCategory = resources.getStringArray(R.array.categories)[position]
            val subCategories = when (selectedCategory) {
                "Tragedy" -> resources.getStringArray(R.array.tragedy)
                "Comedy" -> resources.getStringArray(R.array.comedy)
                "General" -> resources.getStringArray(R.array.general)
                "History" -> resources.getStringArray(R.array.general)
                "Politics" -> resources.getStringArray(R.array.general)
                "Breaking" -> resources.getStringArray(R.array.general)
                "Technology" -> resources.getStringArray(R.array.technology)
                "Science" -> resources.getStringArray(R.array.science)
                "Health" -> resources.getStringArray(R.array.health)
                "Business" -> resources.getStringArray(R.array.business)
                "Entertainment" -> resources.getStringArray(R.array.entertainment)
                "Sports" -> resources.getStringArray(R.array.sports)
                "Education" -> resources.getStringArray(R.array.education)
                "Travel" -> resources.getStringArray(R.array.travel)
                "Jobs" -> resources.getStringArray(R.array.jobs)
                "Food" -> resources.getStringArray(R.array.food)
                else -> emptyArray()
            }
            subCategoryInput.setItems(subCategories)
        }
    }

    private fun postArticle() {
        val userDetails: UserDetails? = RetrieveDetailsFromSharedPreferences.getDetails(requireContext(), "UserDetails")
        if (userDetails != null) {
            val userBasics: UserBasics = userDetails.userBasics
            val title = titleInput.text.toString()
            val content = mEditor.html
            val mainCategory = categoryInput.text.toString()
            val subCategories = subCategoryInput.text.toString()

            if (title.isEmpty() || content.isEmpty() || mainCategory.isEmpty() || subCategories.isEmpty()) {
                Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@postArticle
            }

            val article = Article(
                title = title,
                content = content,
                author = userBasics,
                mainCategory = mainCategory,
                subCategories = subCategories,
                languageType = "en",
                representativePicture = imagePathForServer
            )

            Log.d("EditorFragment", "Article: $article")
            val articleRepository = ArticleRepository()
            val currentList = sharedViewModel.retrievedArticleList.value?.toMutableList() ?: mutableListOf()
            articleRepository.postArticle(article) { postedArticle ->
                if (postedArticle != null) {
                    currentList.add(postedArticle)
                    sharedViewModel.retrievedArticleList.value = currentList
                    Toast.makeText(context, "Article posted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to post article", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "User details not found", Toast.LENGTH_SHORT).show()
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
                    val compressedImageFile = context?.let { it1 -> Compressor.compress(it1, actualImageFile) {
                        quality(20)
                        format(Bitmap.CompressFormat.WEBP)
                        resolution(560, 320)
                        size(1_00)
                    } }!!
//                    val imageInputStream: InputStream = compressedImageFile.inputStream()
//                    FtpClientUpload.uploadFile("test.webp", imageInputStream)
                    // Update the imagePath to the compressed image path
                    imagePath = compressedImageFile.absolutePath

                    // Display the image address
                    mPreview.text = "Image saved at: $imagePath"
                    if (imagePath != null) {
                        uploadImageToServer(imagePath!!)
                    }
            }
                previewImage.setImageDrawable(imagePath?.let { path ->
                    DisplayImage.getDrawableFromPath(requireContext(), path)
                })
            }
        }
    }

    private fun uploadImageToServer(imagePath: String) {
        val file = File(imagePath)
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        val call = RetrofitClient.instance.uploadImage(body)
        call.enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                if (response.isSuccessful) {
                    val mapResponse = response.body()
                    imagePathForServer = mapResponse?.get("location").toString()
                    Log.d("EditorFragment", "Response: ${mapResponse?.get("location")}")
                } else {
                    Log.e("EditorFragment", "Error: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                Log.e("EditorFragment", "Failure: ${t.message}")
            }
        })
    }

    private fun setBackGround(view: View, isAction: Boolean) {
        if (isAction) {
            view.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_border)
        } else {
            view.background = null
        }
    }
}