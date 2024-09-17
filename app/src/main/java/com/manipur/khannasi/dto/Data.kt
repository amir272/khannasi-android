package com.manipur.khannasi.dto

import android.graphics.drawable.Drawable
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime


data class Item(
    val id: Long = 0,
    var text: String = "",
    var image: Drawable? = null
)

data class UserBasics(
    val userId: Long = 0,
    val firstName: String = "",
    val lastName: String? = null,
    val username: String = "",
    val profilePictureUrl: String? = null,
    val userLevel: String = "user"
)

data class UserDetails(
    val id: Long = 0,
    val userBasics: UserBasics = UserBasics(),
    val email: String = "",
    val contactNo: String = "",
    val address: String = "",
    val state: String,
    val country: String,
    val passwordHash: String = "",
    val isVerified: Boolean = false,
    val points: Long = 0
)

data class Article(
    val articleId: Long = 0,
    val title: String = "",
    val content: String = "",
    val author: UserBasics = UserBasics(),
    val publishedDate: String? = null,
    val mainCategory: String = "",
    val subCategories: String = "",
    val languageType: String = "",
    val representativePicture: String? = null,
    val comments: List<ArticleComment>? = null,
    val likeCount: Int = 0,
    val dislikeCount: Int = 0,
    val bookmarkCount: Int = 0

)

data class ArticleBookmark(
    val bookmarkId: Long = 0,
    val articleId: Long,
    val user: UserBasics,
    val createdAt: String? = null
)

data class ArticleComment(
    val commentId: Long = 0,
    val articleId: Long = 0,
    val userBasics: UserBasics = UserBasics(),
    val content: String = "",
    val timestamp: String? = null,
    val replyTo: Long = 0,
    val deleted: Boolean = false
)

data class ArticleVote(
    val voteId: Long = 0,
    val commentId: Long = 0,
    val userBasics: UserBasics = UserBasics(),
    val voteType: String = "",
    val originalPostId: Long = 0
)

data class Discussion(
    val discussionId: Long = 0,
    val title: String = "",
    val description: String = "",
    val initiator: UserBasics = UserBasics(),
    val creationDate: String? = null,
    val approvalCount: Int = 0,
    val mainCategory: String = "",
    val subCategories: String = "",
    val languageType: String = "",
    val representativePicture: String? = null
)

data class DiscussionComment(
    val commentId: Long = 0,
    val discussionId: Long = 0,
    val userBasics: UserBasics = UserBasics(),
    val content: String = "",
    val timestamp: String? = null,
    val replyTo: Long = 0,
    val deleted: Boolean = false
)

data class DiscussionVote(
    val voteId: Long = 0,
    val commentId: Long = 0,
    val userBasics: UserBasics = UserBasics(),
    val voteType: String = "",
    val originalPostId: Long = 0
)