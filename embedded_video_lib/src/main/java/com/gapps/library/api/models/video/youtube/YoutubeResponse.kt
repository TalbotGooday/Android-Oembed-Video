package com.gapps.library.api.models.video.youtube

import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.api.models.video.base.BaseVideoResponse
import com.google.gson.annotations.SerializedName

class YoutubeResponse : BaseVideoResponse {
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

    override fun toPreview(
        url: String?,
        linkToPlay: String,
        hostingName: String,
        videoId: String
    ): VideoPreviewModel {
        return VideoPreviewModel(url, linkToPlay, hostingName, videoId).apply {
            this.videoTitle = this@YoutubeResponse.title
            this.thumbnailUrl = this@YoutubeResponse.thumbnailUrl
            this.width = this@YoutubeResponse.width
            this.height = this@YoutubeResponse.height
        }
    }
}