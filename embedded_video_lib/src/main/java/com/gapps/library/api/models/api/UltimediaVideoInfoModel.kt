package com.gapps.library.api.models.api

import com.gapps.library.api.FORMAT
import com.gapps.library.api.FORMAT_JSON
import com.gapps.library.api.URL
import com.gapps.library.api.models.api.base.VideoInfoModel
import com.gapps.library.api.models.video.ultimedia.UltimediaResponse

class UltimediaVideoInfoModel : VideoInfoModel<UltimediaResponse>() {
    override val baseUrl: String
        get() = "https://www.ultimedia.com"

    //https://regex101.com/r/2AsrOc/1
    override val pattern: String
        get() = ULTIMEDIA_PATTERN
    override val idPattern: String
        get() = pattern
    override val type: Class<UltimediaResponse>
        get() = UltimediaResponse::class.java
    override val hostingName: String
        get() = ULTIMEDIA_HOST_NAME

    override fun getInfoUrl(incomingUrl: String?): String? {
        return "$baseUrl/api/search/oembed?$FORMAT=$FORMAT_JSON&$URL=$incomingUrl"
    }

    override fun getPlayLink(videoId: String): String {
        return "https://www.ultimedia.com/deliver/generic/iframe/src/$videoId/"
    }
}