package com.manipur.khannasi.service

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.manipur.khannasi.dto.ArticleVote
import com.manipur.khannasi.dto.UserBasics
import com.manipur.khannasi.repository.ArticleVoteRepository
import com.manipur.khannasi.roomrepo.ArticleVoteRepo
import com.manipur.khannasi.util.MapToRoomEntity.mapToRoomArticleVote
import kotlinx.coroutines.launch

object PostArticleVoteService {
    private val articleVoteRepository = ArticleVoteRepository()

    fun postArticleVote(articleId: Long, commentId: Long, likeDislike: String, userBasics: UserBasics, articleVoteRepo: ArticleVoteRepo, fragment: Fragment): Boolean {
        var posted = false
        val articleVote = ArticleVote(
            voteType = likeDislike,
            originalPostId = articleId,
            commentId = commentId,
            userBasics = userBasics
        )
        Log.d("PostArticleVoteService", "Posting vote $articleVote")
        articleVoteRepository.postArticleVote(articleVote) { postedVote ->
            if (postedVote != null) {
                fragment.viewLifecycleOwner.lifecycleScope.launch {
                    val savedId = articleVoteRepo.upsertArticleVote(mapToRoomArticleVote(postedVote))
                    Log.d("PostArticleVoteService", "Saved voteId: $savedId")
                }
                posted = true
            }
        }
        return posted
    }
}