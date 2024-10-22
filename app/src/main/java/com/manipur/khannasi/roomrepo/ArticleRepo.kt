package com.manipur.khannasi.roomrepo

import com.manipur.khannasi.roomdao.ArticleDao
import com.manipur.khannasi.roomentity.Article

class ArticleRepo(private val articleDao: ArticleDao) {
    suspend fun insertArticle(article: Article) {
        articleDao.insertArticle(article)
    }

    suspend fun getAllArticles(): List<Article>? {
        return articleDao.getAllArticles()
    }

    suspend fun insertArticles(articles: List<Article>) {
        articleDao.insertArticles(articles)
    }

    suspend fun getArticleById(id: Int): Article? {
        return articleDao.getArticleById(id)
    }
}