package com.gapps.videonoapi.video_utils.ultimedia

import com.gapps.library.api.FORMAT
import com.gapps.library.api.FORMAT_JSON
import com.gapps.library.api.URL
import com.gapps.library.api.models.api.base.VideoInfoModel
import com.gapps.videonoapi.video_utils.ultimedia.response.UltimediaResponse

class UltimediaVideoInfoModel : VideoInfoModel<UltimediaResponse>() {
    override val baseUrl: String
        get() = "https://www.ultimedia.com"

    //https://regex101.com/r/2AsrOc/1
    override val pattern: String
        get() = "(?:http[s]?:\\/\\/)?(?:www)?\\.?ultimedia\\.com\\/(?:deliver|default|api)\\/.*\\/([_a-zA-Z0-9]+)\\S*"
    override val idPattern: String
        get() = pattern
    override val type: Class<UltimediaResponse>
        get() = UltimediaResponse::class.java
    override val hostingName: String
        get() = "Ultimedia"

    override fun getInfoUrl(incomingUrl: String?): String? {
        return "$baseUrl/api/search/oembed?$FORMAT=$FORMAT_JSON&$URL=$incomingUrl"
    }

    override fun getPlayLink(videoId: String): String {
        return "https://www.ultimedia.com/deliver/generic/iframe/src/$videoId/"
    }
}