package com.gapps.library.api.models.video

class VideoPreviewModel {
    var videoTitle: String? = null
    var url: String? = null
    var thumbnailUrl: String? = null
    var videoHosting: String? = null
    var videoId: String? = null
    var linkToPlay: String? = null

    companion object {
        const val ERROR_404 = "Not found"

        const val YOU_TUBE = "YouTube"
        const val YOU_TUBE_MUSIC = "YouTube Music"
        const val VIMEO = "Vimeo"
        const val RUTUBE = "Rutube"

        fun error() = VideoPreviewModel().apply {
            this.videoTitle = ERROR_404
        }
    }
}