package com.manipur.khannasi.util

import androidx.recyclerview.widget.DiffUtil
import com.manipur.khannasi.dto.Discussion

class DiscussionDiffCallback(
    private val oldList: List<Discussion>,
    private val newList: List<Discussion>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Compare unique IDs or other unique properties
        return oldList[oldItemPosition].discussionId == newList[newItemPosition].discussionId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Compare the contents of the items
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}