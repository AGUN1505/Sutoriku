package com.dicoding.sutoriku.data.response.sutori

import com.google.gson.annotations.SerializedName

data class SutoriResponse(
    @field:SerializedName("listStory")
    val listStory: List<ListStoryItem?>? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
