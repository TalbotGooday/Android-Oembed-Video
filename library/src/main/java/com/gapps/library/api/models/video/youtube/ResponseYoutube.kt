package com.gapps.library.api.models.video.youtube

import com.gapps.library.api.YOUTUBE_PATTERN
import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.api.models.video.base.BaseVideoResponse
import com.google.gson.annotations.SerializedName
import java.util.regex.Pattern

class ResponseYoutube: BaseVideoResponse {
	@SerializedName("author_name")
	var authorName: String? = null

	@SerializedName("provider_url")
	var providerUrl: String? = null

	@SerializedName("thumbnail_url")
	var thumbnailUrl: String? = null

	@SerializedName("title")
	var title: String? = null

	@SerializedName("type")
	var type: String? = null

	@SerializedName("version")
	var version: String? = null

	@SerializedName("thumbnail_height")
	var thumbnailHeight: Int = 0

	@SerializedName("author_url")
	var authorUrl: String? = null

	@SerializedName("thumbnail_width")
	var thumbnailWidth: Int = 0

	@SerializedName("width")
	var width: Int = 0

	@SerializedName("html")
	var html: String? = null

	@SerializedName("provider_name")
	var providerName: String? = null

	@SerializedName("height")
	var height: Int = 0

	override fun toPreview(url: String?): VideoPreviewModel {
		return VideoPreviewModel().apply {
			this.videoTitle = this@ResponseYoutube.title
			this.thumbnailUrl = this@ResponseYoutube.thumbnailUrl
			this.url = url
			this.videoHosting = if (url?.contains("music.") == true) {
				VideoPreviewModel.YOUTUBE_MUSIC
			} else {
				VideoPreviewModel.YOUTUBE
			}
			this.videoId = getVideoId(url)
			this.linkToPlay = "https://www.youtube.com/embed/${this.videoId}?autoplay=1&vq=small"
			this.width = this@ResponseYoutube.width
			this.height = this@ResponseYoutube.height
		}
	}

	override fun getVideoId(url: String?): String? {
		url ?: return null

		return YOUTUBE_PATTERN.toRegex().find(url)?.groups?.get(1)?.value
	}
}