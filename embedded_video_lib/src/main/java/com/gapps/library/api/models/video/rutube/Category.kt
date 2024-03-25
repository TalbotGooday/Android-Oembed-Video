package com.gapps.library.api.models.video.rutube


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("category_url")
    val categoryUrl: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = ""
)