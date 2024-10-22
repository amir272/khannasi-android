package com.manipur.khannasi.roomentity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "discussion_vote")
data class DiscussionVote(
    @PrimaryKey val voteId: Long = 0,
    val commentId: Long = 0,
    val voteType: String = "",
    val originalPostId: Long = 0,
    val userId: Long
)