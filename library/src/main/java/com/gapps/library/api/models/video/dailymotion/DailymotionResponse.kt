package com.gapps.library.api.models.video.dailymotion


import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.api.models.video.base.BaseVideoResponse
import com.google.gson.annotations.SerializedName

data class DailymotionResponse(
		@SerializedName("type")
		val type: String = "",
		@SerializedName("version")
		val version: String = "",
		@SerializedName("provider_name")
		val providerName: String = "",
		@SerializedName("provider_url")
		val providerUrl: String = "",
		@SerializedName("title")
		val title: String = "",
		@SerializedName("description")
		val description: String = "",
		@SerializedName("author_name")
		val authorName: String = "",
		@SerializedName("author_url")
		val authorUrl: String = "",
		@SerializedName("width")
		val width: Int = 0,
		@SerializedName("height")
		val height: Int = 0,
		@SerializedName("html")
		val html: String = "",
		@SerializedName("thumbnail_url")
		val thumbnailUrl: String = "",
		@SerializedName("thumbnail_width")
		val thumbnailWidth: Int = 0,
		@SerializedName("thumbnail_height")
		val thumbnailHeight: Int = 0
) : BaseVideoResponse {
	override fun toPreview(url: String?, linkToPlay: String, hostingName: String, videoId: String): VideoPreviewModel {
		return VideoPreviewModel(url, linkToPlay, hostingName, videoId).apply {
			this.thumbnailUrl = this@DailymotionResponse.thumbnailUrl
			this.videoTitle = this@DailymotionResponse.title
			this.width = this@DailymotionResponse.width
			this.height = this@DailymotionResponse.height
		}
	}
}