package com.gapps.library.api.models.api

import com.gapps.library.api.URL
import com.gapps.library.api.models.api.base.VideoInfoModel
import com.gapps.library.api.models.video.streamable.StreamableResponse

class StreamableVideoInfoModel : VideoInfoModel<StreamableResponse>() {
    override val baseUrl: String
        get() = "https://api.streamable.com"
    override val pattern: String
        get() = STREAMABLE_PATTERN
    override val idPattern: String
        get() = pattern
    override val type: Class<StreamableResponse>
        get() = StreamableResponse::class.java
    override val hostingName: String
        get() = STREAMABLE_HOST_NAME

    override fun getInfoUrl(incomingUrl: String?): String? {
        return "$baseUrl/oembed.json?$URL=$incomingUrl"
    }

    override fun getPlayLink(videoId: String): String {
        return "https://streamable.com/o/$videoId"
    }
}