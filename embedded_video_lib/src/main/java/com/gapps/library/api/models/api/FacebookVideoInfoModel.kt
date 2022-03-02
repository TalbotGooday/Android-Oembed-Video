package com.gapps.library.api.models.api

import com.gapps.library.api.FORMAT_JSON
import com.gapps.library.api.URL
import com.gapps.library.api.models.api.base.VideoInfoModel
import com.gapps.library.api.models.video.facebook.FacebookResponse

open class FacebookVideoInfoModel : VideoInfoModel<FacebookResponse>() {
    override val baseUrl: String
        get() = "https://apps.facebook.com"

    //Pattern: https://regex101.com/r/98Nfkr/5
    override val pattern: String
        get() = "(?:http[s]?://)?(?:www.|web.|m.)?(?:facebook|fb)?.com/(?:(?:video.php|watch?/)?\\?v=|.+/videos(?:/.+)?/)(\\d+)[^,;\\s]*"
    override val idPattern: String
        get() = pattern
    override val type: Class<FacebookResponse>
        get() = FacebookResponse::class.java
    override val hostingName: String
        get() = "Facebook"

    override fun getInfoUrl(incomingUrl: String?): String? {
        val id = parseVideoId(incomingUrl)
        return "$baseUrl/plugins/video/oembed.$FORMAT_JSON?$URL=https://www.facebook.com/facebook/videos/$id"
    }

    override fun getPlayLink(videoId: String): String {
        return "https://www.facebook.com/video/embed?video_id=${videoId}"
    }
}