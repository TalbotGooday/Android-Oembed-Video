package com.gapps.library.api.models.video.rutube


import com.google.gson.annotations.SerializedName

data class Restrictions(
    @SerializedName("country")
    val country: Country = Country()
)