package com.manipur.khannasi.roomrepo

import com.manipur.khannasi.roomdao.ArticleVoteDao
import com.manipur.khannasi.roomentity.ArticleVote

class ArticleVoteRepo(private val articleVoteDao: ArticleVoteDao) {
    suspend fun insertArticleVote(articleVote: ArticleVote) {
        articleVoteDao.insertArticleVote(articleVote)
    }

    suspend fun getAllArticleVotes(): List<ArticleVote> {
        return articleVoteDao.getAllArticleVotes()
    }

    suspend fun deleteArticleVote(voteId: Long) {
        articleVoteDao.deleteArticleVote(voteId)
    }

    suspend fun getArticleVoteByArticleId(originalPostId: Long, userId: Long): ArticleVote? {
        return articleVoteDao.getArticleVoteByArticleId(originalPostId, userId)
    }

    suspend fun upsertArticleVote(articleVote: ArticleVote): Long {
        return articleVoteDao.upsertArticleVote(articleVote)
    }

    suspend fun getArticleVoteByPostAndCommentId(originalPostId: Long, commentId: Long, userId: Long): ArticleVote? {
        return articleVoteDao.getArticleVoteByPostAndCommentId(originalPostId, commentId, userId)
    }
}