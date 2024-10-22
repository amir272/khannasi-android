package com.manipur.khannasi.roomentity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article_vote")
data class ArticleVote(
    @PrimaryKey val voteId: Long = 0,
    val commentId: Long = 0,
    val voteType: String = "",
    val originalPostId: Long = 0,
    val userId: Long
)