package com.manipur.khannasi.roomrepo

import com.manipur.khannasi.roomdao.ArticleBookmarkDao
import com.manipur.khannasi.roomentity.ArticleBookmark

class ArticleBookmarkRepo(private val articleBookmarkDao: ArticleBookmarkDao) {
    suspend fun insertArticleBookmark(articleBookmark: ArticleBookmark): Long? {
        return articleBookmarkDao.insertArticleBookmark(articleBookmark)
    }

    suspend fun getAllArticleBookmarks(): List<ArticleBookmark>? {
        return articleBookmarkDao.getAllArticleBookmarks()
    }

    suspend fun getArticleBookmarkByArticleId(articleId: Long, userId: Long): ArticleBookmark? {
        return articleBookmarkDao.getArticleBookmarkByArticleId(articleId, userId)
    }

    suspend fun deleteArticleBookmark(bookmarkId: Long) {
        articleBookmarkDao.deleteArticleBookmark(bookmarkId)
    }

    suspend fun deleteArticleBookmarkByArticleId(articleId: Long, userId: Long) {
        articleBookmarkDao.deleteArticleBookmarkByArticleId(articleId, userId)
    }
}