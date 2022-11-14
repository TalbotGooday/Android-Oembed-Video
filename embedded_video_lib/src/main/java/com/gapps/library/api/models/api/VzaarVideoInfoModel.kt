package com.gapps.library.api.models.api

import com.gapps.library.api.FORMAT_JSON
import com.gapps.library.api.models.api.base.VideoInfoModel
import com.gapps.library.api.models.video.vzaar.VzaarResponse

open class VzaarVideoInfoModel : VideoInfoModel<VzaarResponse>() {
    override val baseUrl: String
        get() = "https://app.vzaar.com"
    override val pattern: String
        get() = VZAAR_PATTERN
    override val idPattern: String
        get() = pattern
    override val type: Class<VzaarResponse>
        get() = VzaarResponse::class.java
    override val hostingName: String
        get() = VZAAR_HOST_NAME

    override fun getInfoUrl(incomingUrl: String?): String? {
        val id = parseVideoId(incomingUrl) ?: return null

        return "$baseUrl/videos/$id.$FORMAT_JSON"
    }

    override fun getPlayLink(videoId: String): String {
        return "https://view.vzaar.com/${videoId}/player"
    }
}