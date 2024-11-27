package com.dicoding.sutoriku.data.response.detail

import com.google.gson.annotations.SerializedName

data class DetailResponse(
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("story")
    val story: Story? = null
)
