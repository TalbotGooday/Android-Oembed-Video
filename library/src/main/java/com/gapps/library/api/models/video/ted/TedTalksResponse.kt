package com.gapps.library.api.models.video.ted


import com.gapps.library.api.TED_TALKS_PATTERN
import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.api.models.video.base.BaseVideoResponse
import com.google.gson.annotations.SerializedName

data class TedTalksResponse(
		@SerializedName("type")
		val type: String = "",
		@SerializedName("version")
		val version: String = "",
		@SerializedName("width")
		val width: Int = 0,
		@SerializedName("height")
		val height: Int = 0,
		@SerializedName("title")
		val title: String = "",
		@SerializedName("description")
		val description: String = "",
		@SerializedName("url")
		val url: String = "",
		@SerializedName("author_name")
		val authorName: String = "",
		@SerializedName("provider_name")
		val providerName: String = "",
		@SerializedName("provider_url")
		val providerUrl: String = "",
		@SerializedName("cache_age")
		val cacheAge: Int = 0,
		@SerializedName("thumbnail_url")
		val thumbnailUrl: String = "",
		@SerializedName("thumbnail_width")
		val thumbnailWidth: Int = 0,
		@SerializedName("thumbnail_height")
		val thumbnailHeight: Int = 0,
		@SerializedName("author_url")
		val authorUrl: String = "",
		@SerializedName("html")
		val html: String = ""
) : BaseVideoResponse {
	override fun toPreview(url: String?): VideoPreviewModel {
		return VideoPreviewModel().apply {
			this.thumbnailUrl = this@TedTalksResponse.thumbnailUrl
			this.videoTitle = this@TedTalksResponse.title
			this.url = url
			this.videoHosting = VideoPreviewModel.TED_TALKS
			this.videoId = getVideoId(url)
			this.linkToPlay = "https://embed.ted.com/talks/$videoId"
			this.width = this@TedTalksResponse.width
			this.height = this@TedTalksResponse.height
		}
	}

	override fun getVideoId(url: String?): String? {
		url ?: return null

		return TED_TALKS_PATTERN.toRegex().find(url)?.groups?.get(1)?.value
	}
}