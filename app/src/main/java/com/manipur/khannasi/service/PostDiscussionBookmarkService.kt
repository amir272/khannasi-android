package com.manipur.khannasi.service

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.manipur.khannasi.constants.NONE
import com.manipur.khannasi.dto.DiscussionBookmark
import com.manipur.khannasi.dto.UserBasics
import com.manipur.khannasi.repository.DiscussionBookmarkRepository
import com.manipur.khannasi.roomrepo.DiscussionBookmarkRepo
import com.manipur.khannasi.util.MapToRoomEntity.mapToRoomDiscussionBookmark
import kotlinx.coroutines.launch

object PostDiscussionBookmarkService {
    private val discussionBookmarkRepository = DiscussionBookmarkRepository()

    fun postDiscussionBookmark(discussionId: Long, book: String, userBasics: UserBasics, discussionBookmarkRepo: DiscussionBookmarkRepo, fragment: Fragment): Boolean {
        var posted = false
        if (book == NONE) {
            Log.d("DiscussionBookmarkService", "Removing bookmark")
            discussionBookmarkRepository.removeBookmark(discussionId, userBasics.userId) {
                fragment.viewLifecycleOwner.lifecycleScope.launch {
                    discussionBookmarkRepo.deleteDiscussionBookmark(discussionId, userBasics.userId)
                }
                posted = true
            }
            return posted
        }
        val discussionVote = DiscussionBookmark(
            discussionId = discussionId,
            user = userBasics
        )
        Log.d("DiscussionBookmarkService", "Adding bookmark")
        discussionBookmarkRepository.bookmarkDiscussion(discussionVote) { postedBookmark ->
            if (postedBookmark != null) {
                fragment.viewLifecycleOwner.lifecycleScope.launch {
                    discussionBookmarkRepo.insertDiscussionBookmark(mapToRoomDiscussionBookmark(postedBookmark))
                }
                posted = true
            }
        }
        return posted
    }
}