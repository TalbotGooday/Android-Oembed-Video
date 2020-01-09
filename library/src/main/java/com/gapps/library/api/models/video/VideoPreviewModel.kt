package com.gapps.library.api.models.video

class VideoPreviewModel {
    var videoTitle: String? = null
    var url: String? = null
    var thumbnailUrl: String? = null
    var videoHosting: String? = null
    var videoId: String? = null
    var linkToPlay: String? = null
    var width = 0
    var height = 0

    companion object {
        const val ERROR_404 = "Not found"

        const val YOUTUBE = "YouTube"
        const val YOUTUBE_MUSIC = "YouTube Music"
        const val VIMEO = "Vimeo"
        const val RUTUBE = "Rutube"
        const val FACEBOOK = "Facebook"

        fun error() = VideoPreviewModel().apply {
            this.videoTitle = ERROR_404
        }
    }
}