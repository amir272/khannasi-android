package com.manipur.khannasi.util

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.manipur.khannasi.dto.Article
import com.manipur.khannasi.dto.ArticleComment
import com.manipur.khannasi.dto.Discussion
import com.manipur.khannasi.dto.DiscussionComment

class SharedViewModel : ViewModel() {
    val isArticleFeedReceived = MutableLiveData<Boolean>()
    val isDiscussionFeedReceived = MutableLiveData<Boolean>()

    val retrievedArticleList = MutableLiveData<List<Article>>()
    val retrievedDiscussionList = MutableLiveData<List<Discussion>>()

    val retrievedArticleComments = MutableLiveData<List<ArticleComment>>()
    val retrievedDiscussionComments = MutableLiveData<List<DiscussionComment>>()
}