package com.gapps.library.api.models.video.rutube


import com.google.gson.annotations.SerializedName

data class PgRating(
    @SerializedName("age")
    val age: Int = 0,
    @SerializedName("logo")
    val logo: String = ""
)