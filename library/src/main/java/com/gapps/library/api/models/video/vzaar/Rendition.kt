package com.gapps.library.api.models.video.vzaar


import com.google.gson.annotations.SerializedName

data class Rendition(
		@SerializedName("type")
		val type: String = "",
		@SerializedName("status_id")
		val statusId: Int = 0,
		@SerializedName("status")
		val status: String = ""
)