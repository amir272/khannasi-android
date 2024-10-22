package com.manipur.khannasi.roomdao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.manipur.khannasi.roomentity.ArticleVote

@Dao
interface ArticleVoteDao {
    @Insert
    suspend fun insertArticleVote(articleVote: ArticleVote)

    @Query("SELECT * FROM article_vote")
    suspend fun getAllArticleVotes(): List<ArticleVote>

    @Query("DELETE FROM article_vote WHERE voteId = :voteId")
    suspend fun deleteArticleVote(voteId: Long)

    @Query("SELECT * FROM article_vote WHERE originalPostId = :originalPostId AND userId = :userId")
    suspend fun getArticleVoteByArticleId(originalPostId: Long, userId: Long): ArticleVote?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertArticleVote(articleVote: ArticleVote): kotlin.Long

    @Query("SELECT * FROM article_vote WHERE originalPostId = :originalPostId AND commentId = :commentId AND userId = :userId")
    suspend fun getArticleVoteByPostAndCommentId(originalPostId: Long, commentId: Long, userId: Long): ArticleVote?

}