package com.gapps.library.api.models.api

import com.gapps.library.api.URL
import com.gapps.library.api.models.api.base.VideoInfoModel
import com.gapps.library.api.models.video.wistia.WistiaResponse

open class WistiaVideoInfoModel : VideoInfoModel<WistiaResponse>() {
    override val baseUrl: String
        get() = "https://fast.wistia.net"
    override val pattern: String
        get() = WISTIA_PATTERN
    override val idPattern: String
        get() = pattern
    override val type: Class<WistiaResponse>
        get() = WistiaResponse::class.java
    override val hostingName: String
        get() = WISTIA_HOST_NAME

    override fun getInfoUrl(incomingUrl: String?): String? {
        incomingUrl ?: return null

        return "$baseUrl/oembed?$URL=$incomingUrl"
    }

    override fun getPlayLink(videoId: String): String {
        return "http://fast.wistia.com/embed/iframe/${videoId}"
    }
}