package com.gapps.library.api.models.video.facebook


import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.api.models.video.base.BaseVideoResponse
import com.google.gson.annotations.SerializedName

data class FacebookResponse(
		@SerializedName("author_name")
		val authorName: String = "",
		@SerializedName("author_url")
		val authorUrl: String = "",
		@SerializedName("provider_url")
		val providerUrl: String = "",
		@SerializedName("provider_name")
		val providerName: String = "",
		@SerializedName("success")
		val success: Boolean = false,
		@SerializedName("height")
		val height: Int? = 0,
		@SerializedName("html")
		val html: String = "",
		@SerializedName("type")
		val type: String = "",
		@SerializedName("version")
		val version: String = "",
		@SerializedName("url")
		val url: String = "",
		@SerializedName("width")
		val width: Int? = 0
) : BaseVideoResponse {
	override fun toPreview(url: String?, linkToPlay: String, hostingName: String, videoId: String): VideoPreviewModel {
		return VideoPreviewModel(url, linkToPlay, hostingName, videoId).apply {
			this.thumbnailUrl = "https://graph.facebook.com/${videoId}/thumbnails"
			this.videoTitle = this@FacebookResponse.authorName
			this.width = this@FacebookResponse.width ?: 0
			this.height = this@FacebookResponse.height ?: 0
		}
	}
}