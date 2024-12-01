package com.dicoding.sutoriku.data.adapter

import android.app.Activity
import android.content.Intent
import android.view.*
import androidx.core.util.Pair
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.dicoding.sutoriku.data.response.sutori.ListStoryItem
import com.dicoding.sutoriku.databinding.ItemRowSutoriBinding
import com.dicoding.sutoriku.ui.detail.DetailActivity

class SutoriAdapter : PagingDataAdapter<ListStoryItem, SutoriAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(val binding: ItemRowSutoriBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.tvItemName.text = story.name
            Glide.with(binding.root)
                .load(story.photoUrl)
                .into(binding.ivItemPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowSutoriBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }

        holder.itemView.setOnClickListener {
            val storyId = story?.id
            val intentId = Intent(holder.itemView.context, DetailActivity::class.java).apply {
                putExtra(DetailActivity.STORY_ID, storyId)
            }

            val optionCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(holder.binding.ivItemPhoto, "imageStoryDetail"),
                    Pair(holder.binding.tvItemName, "nameStoryDetail")
                )

            holder.itemView.context.startActivity(intentId, optionCompat.toBundle())
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ListStoryItem> =
            object : DiffUtil.ItemCallback<ListStoryItem>() {
                override fun areItemsTheSame(
                    oldItem: ListStoryItem,
                    newItem: ListStoryItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: ListStoryItem,
                    newItem: ListStoryItem
                ): Boolean {
                    return oldItem == newItem
                }

            }
    }
}