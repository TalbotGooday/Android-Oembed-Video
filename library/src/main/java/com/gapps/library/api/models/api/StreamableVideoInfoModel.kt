package com.gapps.library.api.models.api

import com.gapps.library.api.URL
import com.gapps.library.api.models.api.base.VideoInfoModel
import com.gapps.library.api.models.video.streamable.StreamableResponse

class StreamableVideoInfoModel:VideoInfoModel<StreamableResponse>() {
    override val baseUrl: String
        get() = "https://api.streamable.com"
    //https://regex101.com/r/2AsrOc/1
    override val pattern: String
        get() = "(?:http[s]?:\\/\\/)?(?:www)?\\.?streamable\\.com\\/([_a-zA-Z0-9]+)\\S*"
    override val idPattern: String
        get() = pattern
    override val type: Class<StreamableResponse>
        get() = StreamableResponse::class.java
    override val hostingName: String
        get() = "Streamable"

    override fun getInfoUrl(incomingUrl: String?): String? {
        return "$baseUrl/oembed.json?$URL=$incomingUrl"
    }

    override fun getPlayLink(videoId: String): String {
        return "https://streamable.com/o/$videoId"
    }
}