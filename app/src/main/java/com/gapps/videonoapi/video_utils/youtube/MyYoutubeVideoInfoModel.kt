package com.gapps.videonoapi.video_utils.youtube

import com.gapps.library.api.FORMAT
import com.gapps.library.api.FORMAT_JSON
import com.gapps.library.api.URL
import com.gapps.library.api.models.api.YoutubeVideoInfoModel
import com.gapps.library.api.models.video.youtube.YoutubeResponse

class MyYoutubeVideoInfoModel : YoutubeVideoInfoModel() {
    override val baseUrl: String
        get() = "https://www.youtube.com"

    //https://regex101.com/r/nJzgG0/1
    override val pattern: String
        get() = "(?:http[s]?:\\/\\/)(?:www.)?(?:m.)?youtu(?:be|.be)?(?:\\.com)?(?:(?:\\w*.?:\\/\\/)?\\w*.?\\w*-?.?\\w*\\/(?:embed|e|v|watch|.*\\/)?\\??(?:feature=\\w*\\.?\\w*)?&?(?:v=)?\\/?)([\\w\\d_-]{11})[^,;\\s]*"
    override val idPattern: String
        get() = pattern
    override val type: Class<YoutubeResponse>
        get() = YoutubeResponse::class.java
    override val hostingName: String
        get() = "YouTube"

    override fun getInfoUrl(incomingUrl: String?): String? {
        val id = parseVideoId(incomingUrl)

        return "$baseUrl/oembed?$FORMAT=$FORMAT_JSON&$URL=https://www.youtube.com/watch?v=$id"
    }

    override fun getPlayLink(videoId: String): String {
        return "https://www.youtube.com/embed/${videoId}?autoplay=0&vq=small&hl=uk"
    }
}