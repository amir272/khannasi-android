package com.manipur.khannasi.service

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.manipur.khannasi.constants.NONE
import com.manipur.khannasi.dto.ArticleBookmark
import com.manipur.khannasi.dto.UserBasics
import com.manipur.khannasi.repository.ArticleBookmarkRepository
import com.manipur.khannasi.roomrepo.ArticleBookmarkRepo
import com.manipur.khannasi.util.MapToRoomEntity.mapToRoomArticleBookmark
import kotlinx.coroutines.launch

object PostArticleBookmarkService {
    private val articleBookmarkRepository = ArticleBookmarkRepository()


    fun postArticleBookmark(articleId: Long, book: String, userBasics: UserBasics, articleBookmarkRepo: ArticleBookmarkRepo, fragment: Fragment): Boolean {
        var posted = false
        if (book == NONE) {
            Log.d("ArticleFeedFragment", "Removing bookmark")
            articleBookmarkRepository.removeBookmark(articleId, userBasics.userId) {
                fragment.viewLifecycleOwner.lifecycleScope.launch {
                    articleBookmarkRepo.deleteArticleBookmarkByArticleId(articleId, userBasics.userId)
                    posted = true
                }
            }
            return posted
        }
        val articleVote = ArticleBookmark(
            articleId = articleId,
            user = userBasics
        )
        articleBookmarkRepository.bookmarkArticle(articleVote) { postedBookmark ->
            if (postedBookmark != null) {
                fragment.viewLifecycleOwner.lifecycleScope.launch {
                    val savedId = articleBookmarkRepo.insertArticleBookmark(mapToRoomArticleBookmark(postedBookmark))
                    Log.d("ArticleFeedFragment", "Saved bookmark with id: $savedId")
                    posted = true
                }
            }
        }
        return posted
    }
}