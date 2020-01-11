package com.gapps.library.api.models.video.dailymotion


import com.gapps.library.api.DAILYMOTION_PATTERN
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
	override fun toPreview(url: String?): VideoPreviewModel {
		return VideoPreviewModel().apply {
			this.thumbnailUrl = this@DailymotionResponse.thumbnailUrl
			this.videoTitle = this@DailymotionResponse.title
			this.url = url
			this.videoHosting = VideoPreviewModel.DAILYMOTION
			this.videoId = getVideoId(url)
			this.linkToPlay = "https://www.dailymotion.com/embed/video/${videoId}"

			this.width = this@DailymotionResponse.width
			this.height = this@DailymotionResponse.height
		}
	}

	override fun getVideoId(url: String?): String? {
		url ?: return null

		return DAILYMOTION_PATTERN.toRegex().find(url)?.groups?.get(1)?.value
	}
}