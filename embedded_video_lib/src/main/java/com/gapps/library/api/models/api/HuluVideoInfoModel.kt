package com.gapps.library.api.models.api

import com.gapps.library.api.FORMAT
import com.gapps.library.api.FORMAT_JSON
import com.gapps.library.api.URL
import com.gapps.library.api.models.api.base.VideoInfoModel
import com.gapps.library.api.models.video.hulu.HuluResponse

open class HuluVideoInfoModel : VideoInfoModel<HuluResponse>() {
    override val baseUrl: String
        get() = "https://www.hulu.com"

    //https://regex101.com/r/LORZgZ/2
    override val pattern: String
        get() = "(?:http[s]?:\\/\\/)?(?:www.)?hulu\\.(?:(?:com\\/\\S*(?:w(?:atch)?|eid)(?:\\/|=)?)|(?:tv\\/))?([a-zA-Z0-9]+)[^,;\\s]*"
    override val idPattern: String
        get() = pattern
    override val type: Class<HuluResponse>
        get() = HuluResponse::class.java
    override val hostingName: String
        get() = "Hulu"

    override fun getInfoUrl(incomingUrl: String?): String? {
        val id = parseVideoId(incomingUrl)
        return "$baseUrl/api/oembed.$FORMAT=$FORMAT_JSON&$URL=http://www.hulu.com/watch/$id"
    }

    override fun getPlayLink(videoId: String): String {
        return "www.hulu.com/embed.html?eid=$videoId"
    }
}