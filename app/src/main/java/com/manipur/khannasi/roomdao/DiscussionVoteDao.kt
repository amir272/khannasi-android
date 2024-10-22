package com.manipur.khannasi.roomdao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.manipur.khannasi.roomentity.DiscussionVote

@Dao
interface DiscussionVoteDao {
    @Insert
    suspend fun insertDiscussionVote(discussionVote: DiscussionVote)

    @Query("SELECT * FROM discussion_vote")
    suspend fun getAllDiscussionVotes(): List<DiscussionVote>

    @Query("DELETE FROM discussion_vote WHERE voteId = :voteId")
    suspend fun deleteDiscussionVote(voteId: Long)

    @Query("SELECT * FROM discussion_vote WHERE originalPostId = :originalPostId AND userId = :userId")
    suspend fun getDiscussionVoteByOriginalPostId(originalPostId: Long, userId: Long): DiscussionVote?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDiscussionVote(discussionVote: DiscussionVote)

    @Query("SELECT * FROM discussion_vote WHERE originalPostId = :originalPostId AND commentId = :commentId AND userId = :userId")
    suspend fun getDiscussionVoteByPostAndCommentId(originalPostId: Long, commentId: Long, userId: Long): DiscussionVote?

}