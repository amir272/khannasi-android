package com.manipur.khannasi.roomentity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article")
data class Article(
    @PrimaryKey val articleId: Long,
    val title: String,
    val content: String,
    val authorId: Long,
    val authorName: String,
    val publishedDate: String,
    val mainCategory: String,
    val subCategories: String,
    val languageType: String,
    val representativePicture: String?,
    val likeCount: Int = 0,
    val dislikeCount: Int = 0,
    val bookmarkCount: Int = 0,
    val viewed: Boolean = false
)