package com.gapps.library.api.models.video.ustream


import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.api.models.video.base.BaseVideoResponse
import com.google.gson.annotations.SerializedName

data class UstreamResponse(
    @SerializedName("provider_url")
    val providerUrl: String = "",
    @SerializedName("html")
    val html: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("author_name")
    val authorName: String = "",
    @SerializedName("height")
    val height: Int = 0,
    @SerializedName("thumbnail_width")
    val thumbnailWidth: Int = 0,
    @SerializedName("width")
    val width: Int = 0,
    @SerializedName("version")
    val version: String = "",
    @SerializedName("author_url")
    val authorUrl: String = "",
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("thumbnail_height")
    val thumbnailHeight: Int = 0
) : BaseVideoResponse {
    override fun toPreview(
        url: String?,
        linkToPlay: String,
        hostingName: String,
        videoId: String
    ): VideoPreviewModel {
        return VideoPreviewModel(url, linkToPlay, hostingName, videoId).apply {
            this.thumbnailUrl = this@UstreamResponse.thumbnailUrl
            this.videoTitle = this@UstreamResponse.title
            this.width = this@UstreamResponse.width
            this.height = this@UstreamResponse.height
        }
    }
}