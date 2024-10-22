package com.manipur.khannasi.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.manipur.khannasi.roomdao.*
import com.manipur.khannasi.roomentity.*

@Database(
    entities = [
        Discussion::class,
        Article::class,
        ArticleBookmark::class,
        ArticleVote::class,
        DiscussionBookmark::class,
        DiscussionVote::class
    ],
    version = 4
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun discussionDao(): DiscussionDao
    abstract fun articleDao(): ArticleDao
    abstract fun articleBookmarkDao(): ArticleBookmarkDao
    abstract fun articleVoteDao(): ArticleVoteDao
    abstract fun discussionBookmarkDao(): DiscussionBookmarkDao
    abstract fun discussionVoteDao(): DiscussionVoteDao
}