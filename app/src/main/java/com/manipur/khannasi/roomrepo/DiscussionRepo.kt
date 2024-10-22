package com.manipur.khannasi.roomrepo

import com.manipur.khannasi.roomdao.DiscussionDao
import com.manipur.khannasi.roomentity.Discussion

class DiscussionRepo(private val discussionDao: DiscussionDao) {
    suspend fun insertDiscussion(discussion: Discussion) {
        discussionDao.insertDiscussion(discussion)
    }

    suspend fun getAllDiscussions(): List<Discussion> {
        return discussionDao.getAllDiscussions()
    }

    suspend fun insertDiscussions(discussions: List<Discussion>) {
        discussionDao.insertDiscussions(discussions)
    }

    suspend fun getDiscussionById(id: Int): Discussion {
        return discussionDao.getDiscussionById(id)
    }
}