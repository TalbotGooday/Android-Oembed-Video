package com.gapps.library.api.models.api

import com.gapps.library.api.FORMAT_JSON
import com.gapps.library.api.URL
import com.gapps.library.api.models.api.base.VideoInfoModel
import com.gapps.library.api.models.video.coub.CoubResponse

open class CoubVideoInfoModel : VideoInfoModel<CoubResponse>() {
    override val baseUrl: String
        get() = "http://coub.com"

    //https://regex101.com/r/ZoQVLa/1
    override val pattern: String
        get() = COUB_PATTERN

    override val idPattern: String
        get() = pattern

    override val type: Class<CoubResponse>
        get() = CoubResponse::class.java

    override val hostingName: String
        get() = COUB_HOST_NAME

    override fun getInfoUrl(incomingUrl: String?): String? {
        incomingUrl ?: return null

        return "$baseUrl/api/oembed.$FORMAT_JSON?$URL=$incomingUrl"
    }

    override fun getPlayLink(videoId: String): String {
        return "https://coub.com/embed/${videoId}"
    }
}