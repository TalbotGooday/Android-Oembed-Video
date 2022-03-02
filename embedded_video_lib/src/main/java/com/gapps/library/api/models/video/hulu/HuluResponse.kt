package com.gapps.library.api.models.video.hulu


import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.api.models.video.base.BaseVideoResponse
import com.google.gson.annotations.SerializedName

data class HuluResponse(
    @SerializedName("provider_name")
    val providerName: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("thumbnail_width")
    val thumbnailWidth: Int = 0,
    @SerializedName("provider_url")
    val providerUrl: String = "",
    @SerializedName("width")
    val width: Int = 0,
    @SerializedName("thumbnail_height")
    val thumbnailHeight: Int = 0,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String = "",
    @SerializedName("cache_age")
    val cacheAge: Int = 0,
    @SerializedName("height")
    val height: Int = 0,
    @SerializedName("version")
    val version: String = "",
    @SerializedName("large_thumbnail_url")
    val largeThumbnailUrl: String = "",
    @SerializedName("html")
    val html: String = "",
    @SerializedName("air_date")
    val airDate: String = "",
    @SerializedName("large_thumbnail_width")
    val largeThumbnailWidth: Int = 0,
    @SerializedName("duration")
    val duration: Double = 0.0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("large_thumbnail_height")
    val largeThumbnailHeight: Int = 0,
    @SerializedName("embed_url")
    val embedUrl: String = "",
    @SerializedName("author_name")
    val authorName: String = "",
) : BaseVideoResponse {
    override fun toPreview(
        url: String?,
        linkToPlay: String,
        hostingName: String,
        videoId: String
    ): VideoPreviewModel {
        return VideoPreviewModel(url, linkToPlay, hostingName, videoId).apply {
            this.thumbnailUrl = this@HuluResponse.largeThumbnailUrl
            this.videoTitle = this@HuluResponse.title
            this.linkToPlay = "https:$embedUrl"
            this.width = this@HuluResponse.width
            this.height = this@HuluResponse.height
        }
    }
}