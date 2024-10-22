package com.manipur.khannasi.fragment

import android.util.Log
import android.widget.Toast
import com.manipur.khannasi.R
import com.manipur.khannasi.dto.Article
import com.manipur.khannasi.dto.UserBasics
import com.manipur.khannasi.dto.UserDetails
import com.manipur.khannasi.repository.ArticleRepository
import com.manipur.khannasi.util.SharedPreferencesRetriever

class PostArticleFragment : BasePostFragment() {

    override fun postContent() {
        val userDetails: UserDetails? = SharedPreferencesRetriever.getDetails(requireContext(), "UserDetails")
        if (userDetails != null) {
            val userBasics: UserBasics = userDetails.userBasics
            val title = titleInput.text.toString()
            val content = mEditor.html
            val mainCategory = categoryInput.text.toString()
            val subCategories = subCategoryInput.text.toString()

            if (title.isEmpty() || content.isEmpty() || mainCategory.isEmpty() || subCategories.isEmpty()) {
                Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return
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

            Log.d("PostArticleFragment", "Article: $article")
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

    override fun getSubCategories(selectedCategory: String): Array<String> {
        return when (selectedCategory) {
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
    }
}