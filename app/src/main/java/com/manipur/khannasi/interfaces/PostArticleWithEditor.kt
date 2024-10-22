package com.manipur.khannasi.interfaces

import com.manipur.khannasi.dto.ArticleComment

interface PostArticleWithEditor {
    fun postArticleComment(articleComment: ArticleComment? = null)
}