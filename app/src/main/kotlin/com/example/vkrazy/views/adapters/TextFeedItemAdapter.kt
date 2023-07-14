package com.example.vkrazy.views.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vkrazy.R
import com.example.vkrazy.data.local.TextFeedItem
import com.example.vkrazy.databinding.TextFeedItemBinding

class TextFeedItemAdapter(private val textItems: MutableList<TextFeedItem>) :
    RecyclerView.Adapter<TextFeedItemAdapter.TextFeedItemViewHolder>() {
    private lateinit var binding: TextFeedItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextFeedItemViewHolder {
        binding = TextFeedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TextFeedItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TextFeedItemViewHolder, position: Int) {
        Log.d(TAG, "position = $position")
        val item = textItems[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return textItems.size
    }

    class TextFeedItemViewHolder(private val binding: TextFeedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TextFeedItem) {
            if (item.userPhoto is String) {
                Glide.with(binding.root).load(item.userPhoto).into(binding.userPhotoImage)
            } else {
                binding.userPhotoImage.setImageResource(R.drawable.person)
            }
            binding.usernameText.text = item.username
            binding.likesText.text = item.likesText
            binding.captionText.text = item.captionText
        }
    }

    fun setData(newData: List<TextFeedItem>) {
        val diffCallback = FeedDiffCallback(textItems, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        textItems.clear()
        textItems.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }

    companion object {
        const val TAG = "TextFeedItemAdapter"
    }
}
