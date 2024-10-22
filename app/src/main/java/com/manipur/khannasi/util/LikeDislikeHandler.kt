package com.manipur.khannasi.util

import android.util.Log
import android.widget.ImageButton
import com.manipur.khannasi.R
import com.manipur.khannasi.constants.DISLIKE
import com.manipur.khannasi.constants.LIKE
import com.manipur.khannasi.roomrepo.ArticleVoteRepo
import com.manipur.khannasi.roomrepo.DiscussionVoteRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object LikeDislikeHandler {


    fun updateLikeDislikeCounts(
        originalPostID: Long,
        commentId: Long,
        myUserId: Long,
        articleCommentVoteRepo: ArticleVoteRepo,
        discussionVoteRepo: DiscussionVoteRepo,
        articleDiscussion: String,
        likeButton: ImageButton,
        dislikeButton: ImageButton,
        onLikeUpdated: (Boolean) -> Unit,
        onDislikeUpdated: (Boolean) -> Unit,
        coroutineScope: CoroutineScope
    ) {
        coroutineScope.launch {
            if (articleDiscussion == "article") {
                Log.d("LikeDislikeHandler", "originalPostID, commentId, userid: $originalPostID, $commentId, $myUserId")
                val vote = articleCommentVoteRepo.getArticleVoteByPostAndCommentId(originalPostID, commentId, myUserId)
                Log.d("LikeDislikeHandler", "vote: $vote")
                withContext(Dispatchers.Main) {
                    vote?.let {
                        val liked = (it.voteType == LIKE)
                        val disliked = (it.voteType == DISLIKE)
                        if (liked) {
                            likedImageResource(likeButton)
                            undislikedImageResource(dislikeButton)
                        } else if (disliked) {
                            dislikedImageResource(dislikeButton)
                            unlikedImageResource(likeButton)
                        } else {
                            unlikedImageResource(likeButton)
                            undislikedImageResource(dislikeButton)
                        }
                        onLikeUpdated(liked)
                        onDislikeUpdated(disliked)
                    }
                }
            } else if (articleDiscussion == "discussion") {
                val vote = discussionVoteRepo.getDiscussionVoteByPostAndCommentId(originalPostID, commentId, myUserId)
                withContext(Dispatchers.Main) {
                    vote?.let {
                        val liked = (it.voteType == LIKE)
                        val disliked = (it.voteType == DISLIKE)
                        if (liked) {
                            likedImageResource(likeButton)
                            undislikedImageResource(dislikeButton)
                        } else if (disliked) {
                            dislikedImageResource(dislikeButton)
                            unlikedImageResource(likeButton)
                        } else {
                            unlikedImageResource(likeButton)
                            undislikedImageResource(dislikeButton)
                        }
                        onLikeUpdated(liked)
                        onDislikeUpdated(disliked)
                    }
                }
            }
        }
    }

    fun handleLikeButtonClick(
        liked: Boolean,
        disliked: Boolean,
        likeButton: ImageButton,
        dislikeButton: ImageButton,
        likeCountValue: Int,
        dislikeCountValue: Int,
        onLikeClick: (Boolean) -> Unit
    ): Pair<Int, Int> {
        var newLikeCount = likeCountValue
        var newDislikeCount = dislikeCountValue

        if (liked) {
            newLikeCount++
            likedImageResource(likeButton)
            if (disliked) {
                newDislikeCount--
                undislikedImageResource(dislikeButton)
            }
        } else {
            newLikeCount--
            unlikedImageResource(likeButton)
        }

        onLikeClick(liked)
        return Pair(newLikeCount, newDislikeCount)
    }

    fun handleDislikeButtonClick(
        disliked: Boolean,
        liked: Boolean,
        likeButton: ImageButton,
        dislikeButton: ImageButton,
        likeCountValue: Int,
        dislikeCountValue: Int,
        onDislikeClick: (Boolean) -> Unit
    ): Pair<Int, Int> {
        var newLikeCount = likeCountValue
        var newDislikeCount = dislikeCountValue

        if (disliked) {
            newDislikeCount++
            dislikedImageResource(dislikeButton)
            if (liked) {
                newLikeCount--
                unlikedImageResource(likeButton)
            }
        } else {
            newDislikeCount--
            undislikedImageResource(dislikeButton)
        }

        onDislikeClick(disliked)
        return Pair(newLikeCount, newDislikeCount)
    }

    private fun likedImageResource(imageButton: ImageButton) {
        imageButton.setImageResource(R.drawable.baseline_keyboard_double_arrow_up_24_blue)
    }

    private fun unlikedImageResource(imageButton: ImageButton) {
        imageButton.setImageResource(R.drawable.baseline_keyboard_double_arrow_up_24)
    }

    private fun dislikedImageResource(imageButton: ImageButton) {
        imageButton.setImageResource(R.drawable.baseline_keyboard_double_arrow_down_24_blue)
    }

    private fun undislikedImageResource(imageButton: ImageButton) {
        imageButton.setImageResource(R.drawable.baseline_keyboard_double_arrow_down_24)
    }
}