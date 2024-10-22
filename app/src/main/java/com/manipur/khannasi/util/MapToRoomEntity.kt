package com.manipur.khannasi.util

import com.manipur.khannasi.dto.Article
import com.manipur.khannasi.dto.Discussion

object MapToRoomEntity {
    fun mapToRoomArticle(article: Article): com.manipur.khannasi.roomentity.Article {
        return com.manipur.khannasi.roomentity.Article(
            articleId = article.articleId,
            title = article.title,
            content = article.content,
            authorId = article.author.userId,
            authorName = article.author.firstName + " " + article.author.lastName,
            publishedDate = article.publishedDate ?: "",
            mainCategory = article.mainCategory,
            subCategories = article.subCategories,
            languageType = article.languageType,
            representativePicture = article.representativePicture,
            likeCount = article.likeCount,
            dislikeCount = article.dislikeCount,
            bookmarkCount = article.bookmarkCount
        )
    }

    fun mapToRoomDiscussion(discussion: Discussion): com.manipur.khannasi.roomentity.Discussion {
        return com.manipur.khannasi.roomentity.Discussion(
            discussionId = discussion.discussionId,
            title = discussion.title,
            description = discussion.description,
            initiatorId = discussion.initiator.userId,
            initiatorName = discussion.initiator.firstName + " " + discussion.initiator.lastName,
            creationDate = discussion.creationDate ?: "",
            mainCategory = discussion.mainCategory,
            subCategories = discussion.subCategories,
            languageType = discussion.languageType,
            approvalCount = discussion.approvalCount,
            bookmarkCount = discussion.bookmarkCount,
            representativePicture = discussion.representativePicture
        )
    }

    fun mapToRoomArticleBookmark(articleBookmark: com.manipur.khannasi.dto.ArticleBookmark): com.manipur.khannasi.roomentity.ArticleBookmark {
        return com.manipur.khannasi.roomentity.ArticleBookmark(
            bookmarkId = articleBookmark.bookmarkId,
            articleId = articleBookmark.articleId,
            userId = articleBookmark.user.userId
        )
    }

    fun mapToRoomDiscussionBookmark(discussionBookmark: com.manipur.khannasi.dto.DiscussionBookmark): com.manipur.khannasi.roomentity.DiscussionBookmark {
        return com.manipur.khannasi.roomentity.DiscussionBookmark(
            bookmarkId = discussionBookmark.bookmarkId,
            discussionId = discussionBookmark.discussionId,
            userId = discussionBookmark.user.userId
        )
    }

    fun mapToRoomArticleVote(articleVote: com.manipur.khannasi.dto.ArticleVote): com.manipur.khannasi.roomentity.ArticleVote {
        return com.manipur.khannasi.roomentity.ArticleVote(
            voteId = articleVote.voteId,
            commentId = articleVote.commentId,
            originalPostId = articleVote.originalPostId,
            voteType = articleVote.voteType,
            userId = articleVote.userBasics.userId
        )
    }

    fun mapToRoomDiscussionVote(discussionVote: com.manipur.khannasi.dto.DiscussionVote): com.manipur.khannasi.roomentity.DiscussionVote {
        return com.manipur.khannasi.roomentity.DiscussionVote(
            voteId = discussionVote.voteId,
            commentId = discussionVote.commentId,
            originalPostId = discussionVote.originalPostId,
            voteType = discussionVote.voteType,
            userId = discussionVote.userBasics.userId
        )
    }
}