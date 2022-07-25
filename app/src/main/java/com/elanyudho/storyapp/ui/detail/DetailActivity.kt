package com.elanyudho.storyapp.ui.detail

import android.view.LayoutInflater
import com.elanyudho.core.abstraction.BaseActivityBinding
import com.elanyudho.storyapp.databinding.ActivityDetailBinding
import com.elanyudho.storyapp.domain.model.Story
import com.elanyudho.storyapp.utils.extensions.convertDate
import com.elanyudho.storyapp.utils.extensions.glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : BaseActivityBinding<ActivityDetailBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityDetailBinding
        get() = { ActivityDetailBinding.inflate(layoutInflater) }

    override fun setupView() {
        initView(getDataIntent())
        setHeaderAction(getDataIntent())
    }

    private fun initView(data: Story) {
        with(binding) {
            tvUsername.text = data.username
            tvCreatedDate.text = data.createdAt?.convertDate()
            tvDesc.text = data.description
            data.imageUrl?.let { ivStory.glide(this@DetailActivity, it) }
        }
    }

    private fun getDataIntent() : Story{
        return intent.getParcelableExtra<Story>(EXTRA_STORY) as Story
    }

    private fun setHeaderAction(data: Story) {
        with(binding) {
            headerDetail.tvTitleHeader.text = data.username
            headerDetail.btnBack.setOnClickListener { onBackPressed() }
        }
    }

    companion object {
        val EXTRA_STORY = "data_story"
    }

}