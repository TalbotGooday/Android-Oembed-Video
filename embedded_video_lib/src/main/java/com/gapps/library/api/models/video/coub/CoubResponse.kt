package com.gapps.library.api.models.video.coub


import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.api.models.video.base.BaseVideoResponse
import com.google.gson.annotations.SerializedName

data class CoubResponse(
    @SerializedName("type")
    val type: String = "",
    @SerializedName("version")
    val version: String = "",
    @SerializedName("width")
    val width: String = "",
    @SerializedName("height")
    val height: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("url")
    val url: String = "",
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String = "",
    @SerializedName("thumbnail_width")
    val thumbnailWidth: String = "",
    @SerializedName("thumbnail_height")
    val thumbnailHeight: String = "",
    @SerializedName("author_name")
    val authorName: String = "",
    @SerializedName("channel_url")
    val channelUrl: String = "",
    @SerializedName("provider_name")
    val providerName: String = "",
    @SerializedName("provider_url")
    val providerUrl: String = "",
    @SerializedName("html")
    val html: String = ""
) : BaseVideoResponse {
    override fun toPreview(
        url: String?,
        linkToPlay: String,
        hostingName: String,
        videoId: String
    ): VideoPreviewModel {
        return VideoPreviewModel(url, linkToPlay, hostingName, videoId).apply {
            this.thumbnailUrl = this@CoubResponse.thumbnailUrl
            this.videoTitle = this@CoubResponse.title
            this.width = this@CoubResponse.width.toInt()
            this.height = this@CoubResponse.height.toInt()
        }
    }
}