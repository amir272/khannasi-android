package com.manipur.khannasi.interfaces

import com.manipur.khannasi.dto.DiscussionComment

interface PostDiscussionWithEditor {
    fun postDiscussionComment(discussionComment: DiscussionComment? = null)
}