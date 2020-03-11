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
		get() = "(?:http[s]?:\\/\\/)(?:www.)?(?:m.)?music.youtu(?:be|.be)?(?:\\.com)?(?:(?:\\w*.?:\\/\\/)?\\w*.?\\w*-?.?\\w*\\/(?:embed|e|v|watch|.*\\/)?\\??(?:feature=\\w*\\.?\\w*)?&?(?:v=)?\\/?)([\\w\\d_-]{11})[^,;\\s]*"
	override val idPattern: String
		get() = pattern
	override val type: Class<YoutubeResponse>
		get() = YoutubeResponse::class.java
	override val hostingName: String
		get() = "YouTube Music"

	override fun getInfoUrl(incomingUrl: String?): String? {
		val id = parseVideoId(incomingUrl)

		return "$baseUrl/oembed?$FORMAT=$FORMAT_JSON&$URL=https://www.youtube.com/watch?v=$id"
	}

	override fun getPlayLink(videoId: String): String {
		return "https://www.youtube.com/embed/${videoId}?autoplay=1&vq=small"
	}
}