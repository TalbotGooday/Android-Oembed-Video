package com.gapps.library.api.models.api

import com.gapps.library.api.DAILYMOTION_PATTERN
import com.gapps.library.api.models.video.dailymotion.DailymotionResponse
import com.gapps.library.utils.getGroupValue

class DailymotionVideoInfoModel : VideoInfoModel<DailymotionResponse> {
	override var pattern: String = DAILYMOTION_PATTERN
	override val idPattern: String
		get() = pattern

	override val type: Class<DailymotionResponse> = DailymotionResponse::class.java

	override fun parseVideoId(url: String?): String? {
		return idPattern.getGroupValue(url, 1)
	}

	override fun checkHostAffiliation(url: String?): Boolean {
		url ?: return false

		return url.matches(pattern.toRegex())
	}
}