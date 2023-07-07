package com.example.vkrazy.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vkrazy.R
import com.example.vkrazy.data.local.FeedItem
import com.example.vkrazy.databinding.FeedItemBinding

class FeedAdapter(private var data: MutableList<FeedItem>) :
    RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {
    private lateinit var binding: FeedItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        binding = FeedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class FeedViewHolder(private val binding: FeedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FeedItem) {
            if (item.userPhoto is String) {
                Glide.with(binding.root).load(item.userPhoto).into(binding.userPhotoImage)
            } else {
                binding.userPhotoImage.setImageResource(R.drawable.person)
            }
            if (item.postImage is String) {
                Glide.with(binding.root).load(item.postImage).into(binding.postImage)
            } else {
                binding.postImage.setImageResource(R.drawable.image_placeholder)
            }
            binding.usernameText.text = item.username
            binding.likesText.text = item.likesText
            binding.captionText.text = item.captionText
        }
    }

    fun setData(newData: List<FeedItem>) {
        val diffCallback = FeedDiffCallback(data, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        data.clear()
        data.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }
}
