package com.example.vkrazy.views.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.vkrazy.data.local.FeedItem

class FeedDiffCallback(
    private val oldList: List<FeedItem>,
    private val newList: List<FeedItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
