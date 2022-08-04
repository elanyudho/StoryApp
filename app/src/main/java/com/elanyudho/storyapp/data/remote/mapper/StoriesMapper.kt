package com.elanyudho.storyapp.data.remote.mapper

import com.elanyudho.core.abstraction.BaseMapper
import com.elanyudho.storyapp.data.remote.response.StoryListResponse
import com.elanyudho.storyapp.domain.model.Story

class StoriesMapper : BaseMapper<StoryListResponse, List<Story>> {

    override fun mapToDomain(raw: StoryListResponse): List<Story> {
        return raw.listStory.map {
            Story(
                imageUrl = it.photoUrl,
                username = it.name,
                createdAt = it.createdAt,
                id = it.id,
                description = it.description,
                long = it.lon,
                lat = it.lat
            )
        }
    }

    override fun mapToRaw(domain: List<Story>): StoryListResponse {
        return StoryListResponse()
    }
}