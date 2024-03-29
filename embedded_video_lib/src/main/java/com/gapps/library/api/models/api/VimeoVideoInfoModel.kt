package com.gapps.library.api.models.api

import com.gapps.library.api.FORMAT_JSON
import com.gapps.library.api.models.api.base.VideoInfoModel
import com.gapps.library.api.models.video.vimeo.VimeoResponse

open class VimeoVideoInfoModel : VideoInfoModel<VimeoResponse>() {
    override val baseUrl: String
        get() = "http://vimeo.com"
    override val pattern: String
        get() = VIMEO_PATTERN
    override val idPattern: String
        get() = pattern
    override val type: Class<VimeoResponse>
        get() = VimeoResponse::class.java
    override val hostingName: String
        get() = VIMEO_HOST_NAME

    override fun getInfoUrl(incomingUrl: String?): String? {
        val id = parseVideoId(incomingUrl) ?: return null

        return "$baseUrl/api/v2/video/$id.$FORMAT_JSON"
    }

    override fun getPlayLink(videoId: String): String {
        return "https://player.vimeo.com/video/${videoId}"
    }
}