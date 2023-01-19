package com.gapps.library.api.models.api

import com.gapps.library.api.URL
import com.gapps.library.api.models.api.base.VideoInfoModel
import com.gapps.library.api.models.video.ustream.UstreamResponse
import com.gapps.library.utils.getGroupValue

open class UstreamVideoInfoModel : VideoInfoModel<UstreamResponse>() {
    override val baseUrl: String
        get() = "https://video.ibm.com"

    //https://regex101.com/r/E0PMAV/2
    override val pattern: String
        get() = USTREAM_PATTERN
    override val idPattern: String
        get() = pattern
    override val type: Class<UstreamResponse>
        get() = UstreamResponse::class.java
    override val hostingName: String
        get() = USTREAM_HOST_NAME

    override fun getInfoUrl(incomingUrl: String?): String? {
        incomingUrl ?: return null

        val id = parseVideoId(incomingUrl)
        val channelId = pattern.getGroupValue(incomingUrl, 2)

        val url = if (id == null || incomingUrl.contains("channel")) {
            "https://ustream.tv/channel/${channelId ?: id}"
        } else {
            "https://ustream.tv/recorded/$id"
        }

        return "$baseUrl/oembed?$URL=$url"
    }

    override fun getPlayLink(videoId: String): String {
        return "http://www.ustream.tv/embed/$videoId"
    }
}