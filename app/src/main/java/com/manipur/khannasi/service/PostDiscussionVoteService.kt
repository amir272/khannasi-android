package com.manipur.khannasi.service

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.manipur.khannasi.dto.DiscussionVote
import com.manipur.khannasi.dto.UserBasics
import com.manipur.khannasi.repository.DiscussionVoteRepository
import com.manipur.khannasi.roomrepo.DiscussionRepo
import com.manipur.khannasi.roomrepo.DiscussionVoteRepo
import com.manipur.khannasi.util.MapToRoomEntity.mapToRoomDiscussionVote
import kotlinx.coroutines.launch

object PostDiscussionVoteService {
    private val discussionVoteRepository = DiscussionVoteRepository()

    fun postDiscussionVote(discussionId: Long, commentId: Long, likeDislike: String, userBasics: UserBasics, discussionVoteRepo: DiscussionVoteRepo, fragment: Fragment): Boolean {
        var posted = false
        val discussionVote = DiscussionVote(
            voteType = likeDislike,
            originalPostId = discussionId,
            commentId = commentId,
            userBasics = userBasics
        )
        Log.d("PostDiscussionVoteService", "Posting vote")
        discussionVoteRepository.postDiscussionVote(discussionVote) { postedVote ->
            if (postedVote != null) {
                fragment.viewLifecycleOwner.lifecycleScope.launch {
                    discussionVoteRepo.upsertDiscussionVote(mapToRoomDiscussionVote(postedVote))
                }
                posted = true
            }
        }
        return posted
    }
}