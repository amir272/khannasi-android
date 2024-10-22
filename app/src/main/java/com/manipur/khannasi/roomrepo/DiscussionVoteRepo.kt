package com.manipur.khannasi.roomrepo

import com.manipur.khannasi.roomdao.DiscussionVoteDao
import com.manipur.khannasi.roomentity.DiscussionVote

class DiscussionVoteRepo(private val discussionVoteDao: DiscussionVoteDao) {
    suspend fun insertDiscussionVote(discussionVote: DiscussionVote) {
        discussionVoteDao.insertDiscussionVote(discussionVote)
    }

    suspend fun getAllDiscussionVotes(): List<DiscussionVote> {
        return discussionVoteDao.getAllDiscussionVotes()
    }

    suspend fun deleteDiscussionVote(voteId: Long) {
        discussionVoteDao.deleteDiscussionVote(voteId)
    }

    suspend fun getDiscussionVoteByDiscussionId(discussionId: Long, userId: Long): DiscussionVote? {
        return discussionVoteDao.getDiscussionVoteByOriginalPostId(discussionId, userId)
    }

    suspend fun upsertDiscussionVote(discussionVote: DiscussionVote) {
        discussionVoteDao.upsertDiscussionVote(discussionVote)
    }

    suspend fun getDiscussionVoteByPostAndCommentId(originalPostId: Long, commentId: Long, userId: Long): DiscussionVote? {
        return discussionVoteDao.getDiscussionVoteByPostAndCommentId(originalPostId, commentId, userId)
    }
}