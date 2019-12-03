package com.gapps.library.api.models.video

class VideoPreviewModel {
    var title: String? = null
    var url: String? = null
    var description: String? = null
    var thumbnailUrl: String? = null
    var html: String? = null
    var type: String? = null
    var videoId: String? = null
    var playLink: String? = null

    companion object {
        const val ERROR_404 = "Not found"
        const val CODE_404 = "404"

        const val YOU_TUBE = "YouTube"
        const val YOU_TUBE_MUSIC = "YouTube Music"
        const val VIMEO = "Vimeo"
        const val RUTUBE = "Rutube"

        fun error() = VideoPreviewModel().apply {
            this.title = ERROR_404
        }
    }
}