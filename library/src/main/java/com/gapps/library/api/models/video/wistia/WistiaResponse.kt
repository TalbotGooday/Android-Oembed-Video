package com.gapps.library.api.models.video.wistia

import com.gapps.library.api.WISTIA_PATTERN
import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.api.models.video.base.BaseVideoResponse
import com.google.gson.annotations.SerializedName

data class WistiaResponse(
		@SerializedName("version")
		val version: String = "",
		@SerializedName("type")
		val type: String = "",
		@SerializedName("html")
		val html: String = "",
		@SerializedName("width")
		val width: Int = 0,
		@SerializedName("height")
		val height: Int = 0,
		@SerializedName("provider_name")
		val providerName: String = "",
		@SerializedName("provider_url")
		val providerUrl: String = "",
		@SerializedName("title")
		val title: String = "",
		@SerializedName("thumbnail_url")
		val thumbnailUrl: String = "",
		@SerializedName("thumbnail_width")
		val thumbnailWidth: Int = 0,
		@SerializedName("thumbnail_height")
		val thumbnailHeight: Int = 0,
		@SerializedName("player_color")
		val playerColor: String = "",
		@SerializedName("duration")
		val duration: Double = 0.0
) : BaseVideoResponse {
	override fun toPreview(url: String?): VideoPreviewModel {
		return VideoPreviewModel().apply {
			this.thumbnailUrl = this@WistiaResponse.thumbnailUrl
			this.videoTitle = this@WistiaResponse.title
			this.url = url
			this.videoHosting = VideoPreviewModel.WISTIA
			this.videoId = getVideoId(url)
			this.linkToPlay = "http://fast.wistia.com/embed/iframe/${videoId}"

			this.width = this@WistiaResponse.width
			this.height = this@WistiaResponse.height
		}
	}

	override fun getVideoId(url: String?): String? {
		url ?: return null

		return WISTIA_PATTERN.toRegex().find(url)?.groups?.get(1)?.value
	}
}