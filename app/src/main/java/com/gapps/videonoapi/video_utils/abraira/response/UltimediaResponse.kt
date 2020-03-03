package com.gapps.videonoapi.video_utils.abraira.response


import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.api.models.video.base.BaseVideoResponse
import com.google.gson.annotations.SerializedName

data class UltimediaResponse(
    @SerializedName("version")
    val version: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("html")
    val html: String = "",
    @SerializedName("width")
    val width: String = "",
    @SerializedName("height")
    val height: String = "",
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String = "",
    @SerializedName("thumbnail_width")
    val thumbnailWidth: String = "",
    @SerializedName("thumbnail_height")
    val thumbnailHeight: String = "",
    @SerializedName("provider_name")
    val providerName: String = "",
    @SerializedName("provider_url")
    val providerUrl: String = "",
    @SerializedName("author_name")
    val authorName: String = ""
): BaseVideoResponse{
    override fun toPreview(url: String?, linkToPlay: String, hostingName: String, videoId: String): VideoPreviewModel {
        return VideoPreviewModel(url, linkToPlay, hostingName, videoId).apply {
            this.thumbnailUrl = this@UltimediaResponse.thumbnailUrl
            this.videoTitle = this@UltimediaResponse.authorName
            this.width = this@UltimediaResponse.width.toInt()
            this.height = this@UltimediaResponse.height.toInt()
        }
    }
}