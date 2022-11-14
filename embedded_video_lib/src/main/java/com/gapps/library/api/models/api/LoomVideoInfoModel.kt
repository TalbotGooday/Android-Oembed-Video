package com.gapps.library.api.models.api

import com.gapps.library.api.FORMAT
import com.gapps.library.api.FORMAT_JSON
import com.gapps.library.api.URL
import com.gapps.library.api.models.api.base.VideoInfoModel
import com.gapps.library.api.models.video.loom.LoomResponse
import com.gapps.library.api.models.video.ultimedia.UltimediaResponse

open class LoomVideoInfoModel : VideoInfoModel<LoomResponse>() {
    override val baseUrl: String
        get() = "https://www.loom.com"

    //https://regex101.com/r/0TwCJy/1
    override val pattern: String
        get() = LOOM_PATTERN
    override val idPattern: String
        get() = pattern
    override val type: Class<LoomResponse>
        get() = LoomResponse::class.java
    override val hostingName: String
        get() = LOOM_HOST_NAME

    override fun getInfoUrl(incomingUrl: String?): String? {
        return "$baseUrl/v1/oembed?$FORMAT=$FORMAT_JSON&$URL=$incomingUrl"
    }

    override fun getPlayLink(videoId: String): String {
        return "https://www.loom.com/embed/$videoId/"
    }
}