package com.dicoding.sutoriku.data.response.addsutori

import com.google.gson.annotations.SerializedName

data class AddSutoriResponse(
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
