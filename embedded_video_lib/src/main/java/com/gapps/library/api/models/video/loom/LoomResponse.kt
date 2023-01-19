package com.gapps.library.api.models.video.loom


import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.api.models.video.base.BaseVideoResponse
import com.google.gson.annotations.SerializedName

data class LoomResponse(
    @SerializedName("type")
    val type: String = "",
    @SerializedName("version")
    val version: String = "",
    @SerializedName("html")
    val html: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("height")
    val height: Int = 0,
    @SerializedName("width")
    val width: Int = 0,
    @SerializedName("provider_name")
    val providerName: String = "",
    @SerializedName("provider_url")
    val providerUrl: String = "",
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String = "",
    @SerializedName("thumbnail_height")
    val thumbnailHeight: Int = 0,
    @SerializedName("thumbnail_width")
    val thumbnailWidth: Int = 0,
    @SerializedName("duration")
    val duration: Int = 0,
    @SerializedName("description")
    val description: String = ""
) : BaseVideoResponse {
    override fun toPreview(
        url: String?,
        linkToPlay: String,
        hostingName: String,
        videoId: String
    ): VideoPreviewModel {
        return VideoPreviewModel(url, linkToPlay, hostingName, videoId).apply {
            this.thumbnailUrl = this@LoomResponse.thumbnailUrl
            this.videoTitle = this@LoomResponse.title
            this.width = this@LoomResponse.width
            this.height = this@LoomResponse.height
        }
    }
}