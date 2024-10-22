package com.manipur.khannasi.roomdao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.manipur.khannasi.roomentity.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArticle(article: Article)

    @Query("SELECT * FROM article")
    suspend fun getAllArticles(): List<Article>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArticles(articles: List<Article>)

    @Query("SELECT * FROM article WHERE articleId = :id")
    suspend fun getArticleById(id: Int): Article?
}