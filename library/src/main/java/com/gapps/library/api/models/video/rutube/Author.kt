package com.gapps.library.api.models.video.rutube


import com.google.gson.annotations.SerializedName

data class Author(
    @SerializedName("avatar_url")
    val avatarUrl: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("site_url")
    val siteUrl: String = ""
)