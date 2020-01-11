package com.gapps.library.api.models.video.facebook


import com.gapps.library.api.FACEBOOK_PATTERN
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
	override fun toPreview(url: String?): VideoPreviewModel {
		val videoId = getVideoId(url)

		return VideoPreviewModel().apply {
			this.thumbnailUrl = "https://graph.facebook.com/${videoId}/picture"
			this.videoTitle = this@FacebookResponse.authorName
			this.url = this@FacebookResponse.url
			this.videoHosting = VideoPreviewModel.FACEBOOK
			this.videoId = videoId
			this.linkToPlay = "https://www.facebook.com/video/embed?video_id=${videoId}"

			this.width = this@FacebookResponse.width ?: 0
			this.height = this@FacebookResponse.height ?: 0
		}
	}

	override fun getVideoId(url: String?): String? {
		url ?: return null

		return FACEBOOK_PATTERN.toRegex().find(url)?.groups?.get(1)?.value
	}
}