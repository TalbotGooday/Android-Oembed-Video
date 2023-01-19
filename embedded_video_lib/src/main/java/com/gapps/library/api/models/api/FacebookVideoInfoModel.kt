package com.gapps.library.api.models.api

import com.gapps.library.api.URL
import com.gapps.library.api.models.api.base.VideoInfoModel
import com.gapps.library.api.models.video.facebook.FacebookResponse

open class FacebookVideoInfoModel : VideoInfoModel<FacebookResponse>() {
    override val baseUrl: String
        get() = "https://graph.facebook.com/v15.0/"

    //Pattern: https://regex101.com/r/98Nfkr/6
    override val pattern: String
        get() = FACEBOOK_PATTERN
    override val idPattern: String
        get() = pattern
    override val type: Class<FacebookResponse>
        get() = FacebookResponse::class.java
    override val hostingName: String
        get() = FACEBOOK_HOST_NAME

    override fun getInfoUrl(incomingUrl: String?): String? {
        val id = parseVideoId(incomingUrl)
        return "$baseUrl/oembed_page?$URL=https://www.facebook.com/facebook/videos/$id"
    }

    override fun getPlayLink(videoId: String): String {
        return "https://www.facebook.com/video/embed?video_id=${videoId}"
    }
}