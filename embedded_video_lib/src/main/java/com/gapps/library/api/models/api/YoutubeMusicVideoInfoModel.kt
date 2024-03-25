package com.gapps.library.api.models.api

import com.gapps.library.api.FORMAT
import com.gapps.library.api.FORMAT_JSON
import com.gapps.library.api.URL
import com.gapps.library.api.models.api.base.VideoInfoModel
import com.gapps.library.api.models.video.youtube.YoutubeResponse

open class YoutubeMusicVideoInfoModel : VideoInfoModel<YoutubeResponse>() {
    override val baseUrl: String
        get() = "https://www.youtube.com"
    override val pattern: String
        get() = YOUTUBE_MUSIC_PATTERN
    override val idPattern: String
        get() = pattern
    override val type: Class<YoutubeResponse>
        get() = YoutubeResponse::class.java
    override val hostingName: String
        get() = YOUTUBE_MUSIC_HOST_NAME

    override fun getInfoUrl(incomingUrl: String?): String? {
        val id = parseVideoId(incomingUrl)

        return "$baseUrl/oembed?$FORMAT=$FORMAT_JSON&$URL=https://www.youtube.com/watch?v=$id"
    }

    override fun getPlayLink(videoId: String): String {
        return "https://www.youtube.com/embed/${videoId}?autoplay=1&vq=small"
    }
}