package com.manipur.khannasi.roomdao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.manipur.khannasi.roomentity.DiscussionBookmark

@Dao
interface DiscussionBookmarkDao {
    @Insert
    suspend fun insertDiscussionBookmark(discussionBookmark: DiscussionBookmark) : Long?

    @Query("SELECT * FROM discussion_bookmark")
    suspend fun getAllDiscussionBookmarks(): List<DiscussionBookmark>

    @Query("DELETE FROM discussion_bookmark WHERE discussionId = :discussionId AND userId = :userId")
    suspend fun deleteDiscussionBookmark(discussionId: Long, userId: Long)

    @Query("SELECT * FROM discussion_bookmark WHERE discussionId = :discussionId AND userId = :userId")
    suspend fun getDiscussionBookmarkByDiscussionId(discussionId: Long, userId: Long): DiscussionBookmark?
}