package com.manipur.khannasi.util

import androidx.recyclerview.widget.DiffUtil
import com.manipur.khannasi.dto.ArticleComment

class ArticleCommentDiffCallback(
    private val oldList: List<ArticleComment>,
    private val newList: List<ArticleComment>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Compare unique IDs or other unique properties
        return oldList[oldItemPosition].commentId == newList[newItemPosition].commentId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Compare the contents of the items
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}