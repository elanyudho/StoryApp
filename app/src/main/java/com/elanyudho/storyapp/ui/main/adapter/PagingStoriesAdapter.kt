package com.elanyudho.storyapp.ui.main.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.elanyudho.storyapp.data.remote.response.StoryListResponse
import com.elanyudho.storyapp.databinding.ItemStoryBinding
import com.elanyudho.storyapp.domain.model.Story
import com.elanyudho.storyapp.utils.extensions.convertDate
import com.elanyudho.storyapp.utils.extensions.glide

class PagingStoriesAdapter :
    PagingDataAdapter<StoryListResponse.Story, PagingStoriesAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    private lateinit var onClick: (data: Story, optionCompat: ActivityOptionsCompat) -> Unit

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    inner class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoryListResponse.Story) {
            with(binding) {
                Log.d("dataku", data.toString())
                tvUsername.text = data.name
                tvCreatedDate.text = data.createdAt.convertDate()
                ivStory.glide(itemView, data.photoUrl)

                root.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            androidx.core.util.Pair(tvUsername, "username"),
                            androidx.core.util.Pair(ivStory, "photo"),
                            androidx.core.util.Pair(tvCreatedDate, "time"),
                            androidx.core.util.Pair(icProfile, "ic_profile")
                        )
                    val story = Story(
                        id = data.id,
                        imageUrl = data.photoUrl,
                        username = data.name,
                        description = data.description,
                        createdAt = data.createdAt,
                        long = data.lon,
                        lat = data.lat
                    )
                    onClick.invoke(story, optionsCompat)
                }
            }
        }
    }

    fun setOnClickData(onClick: (data: Story, optionCompat: ActivityOptionsCompat) -> Unit) {
        this.onClick = onClick
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryListResponse.Story>() {
            override fun areItemsTheSame(
                oldItem: StoryListResponse.Story,
                newItem: StoryListResponse.Story
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoryListResponse.Story,
                newItem: StoryListResponse.Story
            ): Boolean {
                return when {
                    oldItem.id == newItem.id -> true
                    oldItem.createdAt == newItem.createdAt -> true
                    else -> false
                }
            }
        }
    }
}