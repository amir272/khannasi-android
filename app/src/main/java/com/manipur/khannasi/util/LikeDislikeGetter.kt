package com.manipur.khannasi.util

import com.manipur.khannasi.dto.ArticleVote
import com.manipur.khannasi.constants.DISLIKE
import com.manipur.khannasi.constants.LIKE
import com.manipur.khannasi.constants.NO_VOTE

class LikeDislikeGetter {
    companion object {
        fun getLikeCount(articleVotesList: List<ArticleVote>): Int {
            var likeCount = 0
            for (articleVote in articleVotesList) {
                if (articleVote.voteType == LIKE) {
                    likeCount++
                }
            }
            return likeCount
        }

        fun getDislikeCount(articleVotesList: List<ArticleVote>): Int {
            var dislikeCount = 0
            for (articleVote in articleVotesList) {
                if (articleVote.voteType == DISLIKE) {
                    dislikeCount++
                }
            }
            return dislikeCount
        }

        fun getNoVoteCount(articleVotesList: List<ArticleVote>): Int {
            var noVoteCount = 0
            for (articleVote in articleVotesList) {
                if (articleVote.voteType == NO_VOTE) {
                    noVoteCount++
                }
            }
            return noVoteCount
        }
    }
}