package com.elanyudho.storyapp.ui.main.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import com.elanyudho.core.abstraction.BaseViewHolder
import com.elanyudho.core.abstraction.PagingRecyclerviewAdapter
import com.elanyudho.storyapp.databinding.ItemStoryBinding
import com.elanyudho.storyapp.domain.model.Story
import com.elanyudho.storyapp.utils.extensions.convertDate
import com.elanyudho.storyapp.utils.extensions.glide

class StoriesAdapter: PagingRecyclerviewAdapter<StoriesAdapter.StoriesViewHolder, Story>() {

    private lateinit var onClick: (data: Story, optionCompat: ActivityOptionsCompat) -> Unit

    inner class StoriesViewHolder(itemView: ItemStoryBinding) :
        BaseViewHolder<Story, ItemStoryBinding>(itemView) {
        override fun bind(data: Story) {
            with(binding) {

                tvUsername.text = data.username
                tvCreatedDate.text = data.createdAt?.convertDate()
                ivStory.glide(itemView, data.imageUrl?: "")

                root.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        androidx.core.util.Pair(tvUsername, "username"),
                        androidx.core.util.Pair(ivStory, "photo"),
                        androidx.core.util.Pair(tvCreatedDate, "time"),
                        androidx.core.util.Pair(icProfile, "ic_profile")
                    )
                    onClick.invoke(data, optionsCompat)
                }
            }
        }

    }

    override val holderInflater: (LayoutInflater, ViewGroup, Boolean) -> StoriesViewHolder
        get() = { inflater, viewGroup, boolean ->
            StoriesViewHolder(
                ItemStoryBinding.inflate(
                    inflater,
                    viewGroup,
                    boolean
                )
            )
        }

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    fun setOnClickData(onClick: (data: Story, optionCompat: ActivityOptionsCompat) -> Unit) {
        this.onClick = onClick
    }
}