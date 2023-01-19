package com.gapps.library.api.models.api

import com.gapps.library.api.FORMAT
import com.gapps.library.api.FORMAT_JSON
import com.gapps.library.api.URL
import com.gapps.library.api.models.api.base.VideoInfoModel
import com.gapps.library.api.models.video.rutube.RutubeResponse

open class RutubeVideoInfoModel : VideoInfoModel<RutubeResponse>() {
    override val baseUrl: String
        get() = "http://rutube.ru"
    override val pattern: String
        get() = RUTUBE_PATTERN
    override val idPattern: String
        get() = pattern
    override val type: Class<RutubeResponse>
        get() = RutubeResponse::class.java
    override val hostingName: String
        get() = RUTUBE_HOST_NAME

    override fun getInfoUrl(incomingUrl: String?): String? {
        val id = parseVideoId(incomingUrl) ?: return null

        return if (id.length < 32) {
            "$baseUrl/api/oembed?$FORMAT=$FORMAT_JSON&$URL=$id"
        } else {
            "$baseUrl/api/video/$id/"
        }
    }

    override fun getPlayLink(videoId: String): String {
        return "http://rutube.ru/play/embed/${videoId}"
    }
}