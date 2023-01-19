package com.gapps.library.api.models.video

class VideoPreviewModel private constructor() {
    var url: String? = null
    var videoTitle: String? = null
    var thumbnailUrl: String? = null
    var videoHosting: String? = null
    var videoId: String? = null
    var linkToPlay: String? = null
    var width = 0
    var height = 0
    var errorMessage: String? = null

    constructor(url: String?, linkToPlay: String, hostingName: String, videoId: String) : this() {
        this.url = url
        this.videoHosting = hostingName
        this.videoId = videoId
        this.linkToPlay = linkToPlay
    }

    class Builder(private val model: VideoPreviewModel = VideoPreviewModel()) {

        fun setUrl(url: String) = apply { model.url = url }
        fun setVideoTitle(videoTitle: String) = apply { model.videoTitle = videoTitle }
        fun setThumbnailUrl(thumbnailUrl: String) = apply { model.thumbnailUrl = thumbnailUrl }
        fun setVideoHosting(videoHosting: String) = apply { model.videoHosting = videoHosting }
        fun setVideoId(videoId: String) = apply { model.videoId = videoId }
        fun setLinkToPlay(linkToPlay: String) = apply { model.linkToPlay = linkToPlay }
        fun setWidth(width: Int) = apply { model.width = width }
        fun setHeight(height: Int) = apply { model.height = height }
        fun setErrorMessage(errorMessage: String) = apply { model.errorMessage = errorMessage }

        fun build(): VideoPreviewModel {
            return model
        }
    }

    companion object {
        private const val ERROR_404 = "Not found"

        fun error() = VideoPreviewModel().apply {
            this.videoTitle = ERROR_404
        }
    }
}