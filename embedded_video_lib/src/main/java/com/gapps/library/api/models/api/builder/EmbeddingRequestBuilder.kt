package com.gapps.library.api.models.api.builder

import com.gapps.library.api.models.api.base.VideoInfoModel

class EmbeddingRequest private constructor(
    val originalUrl: String,
    var videoInfoModel: VideoInfoModel<*>? = null
) {
    class Builder {
        private var url: String? = null
        private var videoInfoModel: VideoInfoModel<*>? = null

        fun setUrl(url: String) = apply { this.url = url }

        fun setVideoInfoModel(videoInfoModel: VideoInfoModel<*>) =
            apply { this.videoInfoModel = videoInfoModel }

        fun build(): EmbeddingRequest {
            return EmbeddingRequest(url!!)
        }
    }
}