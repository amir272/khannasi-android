package com.manipur.khannasi.roomentity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article_bookmark")
data class ArticleBookmark(
    @PrimaryKey val bookmarkId: Long,
    val articleId: Long,
    val userId: Long
)