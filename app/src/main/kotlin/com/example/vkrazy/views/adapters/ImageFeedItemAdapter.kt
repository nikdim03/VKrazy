package com.example.vkrazy.views.adapters

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vkrazy.R
import com.example.vkrazy.data.local.ImageFeedItem
import com.example.vkrazy.databinding.ImageFeedItemBinding

class ImageFeedItemAdapter(private val photoItems: MutableList<ImageFeedItem>) :
    RecyclerView.Adapter<ImageFeedItemAdapter.ImageFeedItemViewHolder>() {
    private lateinit var binding: ImageFeedItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageFeedItemViewHolder {
        binding = ImageFeedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageFeedItemViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ImageFeedItemViewHolder, position: Int) {
        Log.d(TAG, "position = $position")
        val item = photoItems[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return photoItems.size
    }

    class ImageFeedItemViewHolder(private val binding: ImageFeedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ImageFeedItem) {
            if (item.userPhoto is String) {
                Glide.with(binding.root).load(item.userPhoto).into(binding.userPhotoImage)
            } else {
                binding.userPhotoImage.setImageResource(R.drawable.person)
            }
            if (item.postImage is String) {
                if (item.postImage.startsWith("https://")) {
                    Glide.with(binding.root).load(item.postImage).into(binding.postImage)
                } else {
                    // Get the bitmap from the path
                    val bitmap = BitmapFactory.decodeFile(item.postImage)
                    // Set the bitmap to the post image view
                    binding.postImage.setImageBitmap(bitmap)
                }
            } else {
                binding.postImage.setImageResource(R.drawable.image_placeholder)
            }
            binding.usernameText.text = item.username
            binding.likesText.text = item.likesText
            binding.captionText.text = item.captionText
        }
    }

    fun setData(newData: List<ImageFeedItem>) {
        val diffCallback = FeedDiffCallback(photoItems, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        photoItems.clear()
        photoItems.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }

    companion object {
        const val TAG = "ImageFeedItemAdapter"
    }
}
