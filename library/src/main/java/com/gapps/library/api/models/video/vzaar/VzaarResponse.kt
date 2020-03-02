package com.gapps.library.api.models.video.vzaar


import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.api.models.video.base.BaseVideoResponse
import com.google.gson.annotations.SerializedName

data class VzaarResponse(
		@SerializedName("type")
		val type: String = "",
		@SerializedName("version")
		val version: String = "",
		@SerializedName("width")
		val width: Int? = 0,
		@SerializedName("height")
		val height: Int? = 0,
		@SerializedName("html")
		val html: String = "",
		@SerializedName("video_status_id")
		val videoStatusId: Int = 0,
		@SerializedName("video_status_description")
		val videoStatusDescription: String = "",
		@SerializedName("play_count")
		val playCount: Int = 0,
		@SerializedName("total_size")
		val totalSize: Int = 0,
		@SerializedName("title")
		val title: String = "",
		@SerializedName("description")
		val description: String = "",
		@SerializedName("author_name")
		val authorName: String = "",
		@SerializedName("author_url")
		val authorUrl: String = "",
		@SerializedName("author_account")
		val authorAccount: Int = 0,
		@SerializedName("provider_name")
		val providerName: String = "",
		@SerializedName("provider_url")
		val providerUrl: String = "",
		@SerializedName("video_url")
		val videoUrl: String = "",
		@SerializedName("thumbnail_url")
		val thumbnailUrl: String = "",
		@SerializedName("thumbnail_width")
		val thumbnailWidth: String = "",
		@SerializedName("thumbnail_height")
		val thumbnailHeight: String = "",
		@SerializedName("framegrab_url")
		val framegrabUrl: String = "",
		@SerializedName("framegrab_width")
		val framegrabWidth: Int = 0,
		@SerializedName("framegrab_height")
		val framegrabHeight: Int = 0,
		@SerializedName("duration")
		val duration: Double = 0.0,
		@SerializedName("renditions")
		val renditions: List<Rendition> = listOf(),
		@SerializedName("categories")
		val categories: List<Any> = listOf()
) : BaseVideoResponse {
	override fun toPreview(url: String?, linkToPlay: String, hostingName: String, videoId: String): VideoPreviewModel {
		return VideoPreviewModel(url, linkToPlay, hostingName, videoId).apply {
			this.thumbnailUrl = this@VzaarResponse.framegrabUrl
			this.videoTitle = this@VzaarResponse.title
			this.width = this@VzaarResponse.width ?: 0
			this.height = this@VzaarResponse.height ?: 0
		}
	}
}