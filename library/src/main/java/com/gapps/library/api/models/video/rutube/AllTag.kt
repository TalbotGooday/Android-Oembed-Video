package com.gapps.library.api.models.video.rutube


import com.google.gson.annotations.SerializedName

data class AllTag(
		@SerializedName("comment")
		val comment: String = "",
		@SerializedName("id")
		val id: Int = 0,
		@SerializedName("name")
		val name: String = "",
		@SerializedName("type")
		val type: String = "",
		@SerializedName("url")
		val url: String = ""
)