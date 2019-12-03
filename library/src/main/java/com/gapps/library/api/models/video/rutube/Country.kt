package com.gapps.library.api.models.video.rutube


import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("allowed")
    val allowed: List<String> = listOf(),
    @SerializedName("restricted")
    val restricted: List<Any> = listOf()
)