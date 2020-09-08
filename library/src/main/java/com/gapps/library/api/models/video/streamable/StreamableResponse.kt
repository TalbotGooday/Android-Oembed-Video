package com.gapps.library.api.models.video.streamable

import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.api.models.video.base.BaseVideoResponse
import com.google.gson.annotations.SerializedName

data class StreamableResponse(
    @SerializedName("version")
    val version: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("html")
    val html: String = "",
    @SerializedName("width")
    val width: String = "",
    @SerializedName("height")
    val height: String = "",
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String = "",
    @SerializedName("provider_name")
    val providerName: String = "",
    @SerializedName("provider_url")
    val providerUrl: String = ""
): BaseVideoResponse {
    override fun toPreview(url: String?, linkToPlay: String, hostingName: String, videoId: String): VideoPreviewModel {
        return VideoPreviewModel(url, linkToPlay, hostingName, videoId).apply {
            this.thumbnailUrl = "https:${this@StreamableResponse.thumbnailUrl.trim()}"
            this.videoTitle = this@StreamableResponse.title
            this.width = this@StreamableResponse.width.toInt()
            this.height = this@StreamableResponse.height.toInt()
        }
    }
}