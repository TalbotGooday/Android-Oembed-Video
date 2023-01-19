package com.gapps.library.api.models.api

import com.gapps.library.api.URL
import com.gapps.library.api.models.api.base.VideoInfoModel
import com.gapps.library.api.models.video.dailymotion.DailymotionResponse

open class DailymotionVideoInfoModel : VideoInfoModel<DailymotionResponse>() {
    override val baseUrl: String
        get() = "https://www.dailymotion.com"

    override val pattern: String
        get() = DAILYMOTION_PATTERN
    override val idPattern: String
        get() = pattern

    override val type: Class<DailymotionResponse> = DailymotionResponse::class.java

    override val hostingName: String
        get() = DAILYMOTION_HOST_NAME

    override fun getInfoUrl(incomingUrl: String?): String? {
        incomingUrl ?: return null

        val id = parseVideoId(incomingUrl)
        return "$baseUrl/services/oembed/?$URL=https://www.dailymotion.com/video/$id"
    }

    override fun getPlayLink(videoId: String): String {
        return "https://www.dailymotion.com/embed/video/${videoId}"
    }
}