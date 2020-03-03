package com.gapps.library.api.models.api

import com.gapps.library.api.FORMAT_JSON
import com.gapps.library.api.models.api.base.VideoInfoModel
import com.gapps.library.api.models.video.vzaar.VzaarResponse

class VzaarVideoInfoModel : VideoInfoModel<VzaarResponse>() {
	override val baseUrl: String
		get() = "https://app.vzaar.com/"
	override val pattern: String
		get() = "(?:http[s]?://)?(?:.+)?vzaar.com/?(?:videos/)?([0-9]+)[^,;\\s]*"
	override val idPattern: String
		get() = pattern
	override val type: Class<VzaarResponse>
		get() = VzaarResponse::class.java
	override val hostingName: String
		get() = "Vzaar"

	override fun getInfoUrl(incomingUrl: String?): String? {
		val id = parseVideoId(incomingUrl) ?: return null

		return "$baseUrl/videos/$id.$FORMAT_JSON"
	}

	override fun getPlayLink(videoId: String): String {
		return "https://view.vzaar.com/${videoId}/player"
	}
}