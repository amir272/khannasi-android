package com.manipur.khannasi.roomentity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "discussion_bookmark")
data class DiscussionBookmark(
    @PrimaryKey val bookmarkId: Long = 0,
    val discussionId: Long,
    val userId: Long
)