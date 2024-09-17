package com.manipur.khannasi.misc

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.manipur.khannasi.dto.Article
import com.manipur.khannasi.dto.Item

class SharedViewModel : ViewModel() {

    val htmlTextForPost = MutableLiveData<String>()
    val articleSaved = MutableLiveData<Article>()
    val savedImageDrawable = MutableLiveData<Drawable>()

    val retrievedArticleList = MutableLiveData<List<Article>>()
}