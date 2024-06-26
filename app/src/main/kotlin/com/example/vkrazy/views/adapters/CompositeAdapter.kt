package com.example.vkrazy.views.adapters

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vkrazy.data.local.Equatable
import com.example.vkrazy.data.local.ImageFeedItem
import com.example.vkrazy.data.local.TextFeedItem

class CompositeAdapter(private var data: MutableList<Equatable>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val imageFeedItemAdapter =
        ImageFeedItemAdapter(data.filterIsInstance<ImageFeedItem>() as MutableList<ImageFeedItem>)
    private val textFeedItemAdapter =
        TextFeedItemAdapter(data.filterIsInstance<TextFeedItem>() as MutableList<TextFeedItem>)

    private var imageItemPosition = 0
    private var textItemPosition = 0
    private var prevPos = 0
    fun setData(newData: MutableList<Equatable>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return data.size
            }

            override fun getNewListSize(): Int {
                return newData.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition] == newData[newItemPosition]
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition] == newData[newItemPosition]
            }
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)

        data = newData

        imageFeedItemAdapter.setData(data.filterIsInstance<ImageFeedItem>())
        textFeedItemAdapter.setData(data.filterIsInstance<TextFeedItem>())

        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is ImageFeedItem -> PHOTO_FEED_ITEM_TYPE
            is TextFeedItem -> TEXT_FEED_ITEM_TYPE
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PHOTO_FEED_ITEM_TYPE -> imageFeedItemAdapter.onCreateViewHolder(parent, viewType)
            TEXT_FEED_ITEM_TYPE -> textFeedItemAdapter.onCreateViewHolder(parent, viewType)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder(position = ${holder.absoluteAdapterPosition})")
        when (holder.itemViewType) {
            PHOTO_FEED_ITEM_TYPE -> {
                imageItemPosition = if (prevPos > holder.absoluteAdapterPosition) {
                    imageItemPosition - 1
                } else if (prevPos == holder.absoluteAdapterPosition) {
                    imageItemPosition
                } else {
                    imageItemPosition + 1
                }
                imageFeedItemAdapter.onBindViewHolder(
                    holder as ImageFeedItemAdapter.ImageFeedItemViewHolder,
                    imageItemPosition
                )
                prevPos = holder.absoluteAdapterPosition
            }

            TEXT_FEED_ITEM_TYPE -> {
                textItemPosition = if (prevPos > holder.absoluteAdapterPosition) {
                    textItemPosition - 1
                } else if (prevPos == holder.absoluteAdapterPosition) {
                    textItemPosition
                } else {
                    textItemPosition + 1
                }
                textFeedItemAdapter.onBindViewHolder(
                    holder as TextFeedItemAdapter.TextFeedItemViewHolder,
                    textItemPosition
                )
                prevPos = holder.absoluteAdapterPosition
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    companion object {
        private const val PHOTO_FEED_ITEM_TYPE = 0
        private const val TEXT_FEED_ITEM_TYPE = 1
        private const val TAG = "CompositeAdapter"
    }
}
