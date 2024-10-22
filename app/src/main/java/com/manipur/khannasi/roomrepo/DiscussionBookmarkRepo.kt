package com.manipur.khannasi.roomrepo

import com.manipur.khannasi.roomdao.DiscussionBookmarkDao
import com.manipur.khannasi.roomentity.DiscussionBookmark

class DiscussionBookmarkRepo(private val discussionBookmarkDao: DiscussionBookmarkDao) {
    suspend fun insertDiscussionBookmark(discussionBookmark: DiscussionBookmark): Long? {
        return discussionBookmarkDao.insertDiscussionBookmark(discussionBookmark)
    }

    suspend fun getAllDiscussionBookmarks(): List<DiscussionBookmark> {
        return discussionBookmarkDao.getAllDiscussionBookmarks()
    }

    suspend fun deleteDiscussionBookmark(discussionId: Long, userId: Long) {
        discussionBookmarkDao.deleteDiscussionBookmark(discussionId, userId)
    }

    suspend fun getDiscussionBookmarkByDiscussionId(discussionId: Long, userId: Long): DiscussionBookmark? {
        return discussionBookmarkDao.getDiscussionBookmarkByDiscussionId(discussionId, userId)
    }
}