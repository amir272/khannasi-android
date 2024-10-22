package com.manipur.khannasi.roomdao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.manipur.khannasi.roomentity.ArticleBookmark

@Dao
interface ArticleBookmarkDao {
    @Insert
    suspend fun insertArticleBookmark(articleBookmark: ArticleBookmark): Long?

    @Query("SELECT * FROM article_bookmark")
    suspend fun getAllArticleBookmarks(): List<ArticleBookmark>

    @Query("SELECT * FROM article_bookmark WHERE articleId = :articleId AND userId = :userId")
    suspend fun getArticleBookmarkByArticleId(articleId: Long, userId: Long): ArticleBookmark?

    @Query("DELETE FROM article_bookmark WHERE bookmarkId = :bookmarkId")
    suspend fun deleteArticleBookmark(bookmarkId: Long)

    @Query("DELETE FROM article_bookmark WHERE articleId = :articleId AND userId = :userId")
    suspend fun deleteArticleBookmarkByArticleId(articleId: Long, userId: Long)
}