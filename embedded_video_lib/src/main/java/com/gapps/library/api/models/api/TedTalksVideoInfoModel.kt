package com.gapps.library.api.models.api

import com.gapps.library.api.FORMAT_JSON
import com.gapps.library.api.URL
import com.gapps.library.api.models.api.base.VideoInfoModel
import com.gapps.library.api.models.video.ted.TedTalksResponse

open class TedTalksVideoInfoModel : VideoInfoModel<TedTalksResponse>() {
    override val baseUrl: String
        get() = "https://www.ted.com"
    override val pattern: String
        get() = TED_TALKS_PATTERN
    override val idPattern: String
        get() = pattern
    override val type: Class<TedTalksResponse>
        get() = TedTalksResponse::class.java
    override val hostingName: String
        get() = TED_TALKS_HOST_NAME

    override fun getInfoUrl(incomingUrl: String?): String? {
        return "$baseUrl/services/v1/oembed.$FORMAT_JSON?$URL=$incomingUrl"
    }

    override fun getPlayLink(videoId: String): String {
        return "https://embed.ted.com/talks/$videoId"
    }
}