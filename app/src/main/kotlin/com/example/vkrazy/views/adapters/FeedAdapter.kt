package com.example.vkrazy.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vkrazy.R
import com.example.vkrazy.data.local.FeedItem
import de.hdodenhof.circleimageview.CircleImageView

class FeedAdapter(private var data: MutableList<FeedItem>) :
    RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_item, parent, false)
        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Initialize views using findViewById or View Binding
        private val userPhotoImageView: CircleImageView =
            itemView.findViewById(R.id.user_photo_image)
        private val usernameTextView: TextView = itemView.findViewById(R.id.username_text)
        private val postImageView: ImageView = itemView.findViewById(R.id.post_image)
        private val likesTextView: TextView = itemView.findViewById(R.id.likes_text)
        private val captionTextView: TextView = itemView.findViewById(R.id.caption_text)

        fun bind(item: FeedItem) {
            if (item.userPhoto is String) {
                Glide.with(itemView).load(item.userPhoto).into(userPhotoImageView)
            } else {
                userPhotoImageView.setImageResource(R.drawable.person)
            }
            if (item.postImage is String) {
                Glide.with(itemView).load(item.postImage).into(userPhotoImageView)
            } else {
                postImageView.setImageResource(R.drawable.image_placeholder)
            }
            usernameTextView.text = item.username
            likesTextView.text = item.likesText
            captionTextView.text = item.captionText
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
