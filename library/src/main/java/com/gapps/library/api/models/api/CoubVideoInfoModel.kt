package com.gapps.library.api.models.api

import com.gapps.library.api.FORMAT_JSON
import com.gapps.library.api.URL
import com.gapps.library.api.models.api.base.VideoInfoModel
import com.gapps.library.api.models.video.coub.CoubResponse

class CoubVideoInfoModel : VideoInfoModel<CoubResponse>() {
	override val baseUrl: String
		get() = "http://coub.com/"

	//https://regex101.com/r/ZoQVLa/1
	override val pattern: String
		get() = "(?:http[s]?:\\/\\/)?(?:www)?\\.?coub\\.com\\/(?:embed|view|api)\\/([_a-zA-Z0-9]+)\\S*"

	override val idPattern: String
		get() = pattern

	override val type: Class<CoubResponse>
		get() = CoubResponse::class.java

	override val hostingName: String
		get() = "Coub"

	override fun getInfoUrl(incomingUrl: String?): String? {
		incomingUrl ?: return null

		return "$baseUrl/api/oembed.$FORMAT_JSON?$URL=$incomingUrl"
	}

	override fun getPlayLink(videoId: String): String {
		return "https://coub.com/embed/${videoId}"
	}
}