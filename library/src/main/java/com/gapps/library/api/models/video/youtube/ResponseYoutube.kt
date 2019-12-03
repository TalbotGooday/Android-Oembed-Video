package com.gapps.library.api.models.video.youtube

import com.gapps.library.api.YOUTUBE_PATTERN_ID
import com.gapps.library.api.models.video.VideoPreviewModel
import com.google.gson.annotations.SerializedName
import java.util.regex.Pattern

class ResponseYoutube {
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

	fun toPreview(url: String? = null): VideoPreviewModel {
		return VideoPreviewModel().apply {
			this.title = this@ResponseYoutube.title
			this.thumbnailUrl = this@ResponseYoutube.thumbnailUrl
			this.url = url
			this.type = if (url?.contains("music.") == true) {
				VideoPreviewModel.YOU_TUBE_MUSIC
			} else {
				VideoPreviewModel.YOU_TUBE
			}
			this.videoId = extractId(url)
			this.playLink = "https://www.youtube.com/embed/${this.videoId}?autoplay=1&vq=small"
		}
	}

	fun extractId(url: String?): String? {
		url ?: return null

		val matcher = Pattern.compile(YOUTUBE_PATTERN_ID).matcher(url)
		if (matcher.find()) {
			return matcher.group(1)
		}
		return null
	}
}