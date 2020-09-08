package com.gapps.library.api.models.video.tiktok


import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.api.models.video.base.BaseVideoResponse
import com.google.gson.annotations.SerializedName

data class TikTokResponse(
		@SerializedName("version")
		val version: String = "",
		@SerializedName("type")
		val type: String = "",
		@SerializedName("title")
		val title: String = "",
		@SerializedName("author_url")
		val authorUrl: String = "",
		@SerializedName("author_name")
		val authorName: String = "",
		@SerializedName("width")
		val width: String = "",
		@SerializedName("height")
		val height: String = "",
		@SerializedName("html")
		val html: String = "",
		@SerializedName("thumbnail_width")
		val thumbnailWidth: Int = 0,
		@SerializedName("thumbnail_height")
		val thumbnailHeight: Int = 0,
		@SerializedName("thumbnail_url")
		val thumbnailUrl: String = "",
		@SerializedName("provider_url")
		val providerUrl: String = "",
		@SerializedName("provider_name")
		val providerName: String = ""
) : BaseVideoResponse {
    override fun toPreview(url: String?, linkToPlay: String, hostingName: String, videoId: String): VideoPreviewModel {
        return VideoPreviewModel(url, linkToPlay, hostingName, videoId).apply {
            this.thumbnailUrl = this@TikTokResponse.thumbnailUrl
            this.videoTitle = this@TikTokResponse.authorName
            this.width = this@TikTokResponse.width.toInt()
            this.height = this@TikTokResponse.height.toInt()
        }
    }

}