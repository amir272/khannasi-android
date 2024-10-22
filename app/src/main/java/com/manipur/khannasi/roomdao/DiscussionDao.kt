package com.manipur.khannasi.roomdao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.manipur.khannasi.roomentity.Discussion

@Dao
interface DiscussionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDiscussion(discussion: Discussion)

    @Query("SELECT * FROM discussion")
    suspend fun getAllDiscussions(): List<Discussion>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDiscussions(discussions: List<Discussion>)

    @Query("SELECT * FROM discussion WHERE discussionId = :id")
    suspend fun getDiscussionById(id: Int): Discussion
}