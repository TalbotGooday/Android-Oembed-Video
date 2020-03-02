package com.gapps.library.api.models.api.base

import com.gapps.library.utils.getGroupValue

abstract class VideoInfoModel<T> {
	abstract val baseUrl: String
	abstract val pattern: String
	abstract val idPattern: String
	abstract val type: Class<T>

	abstract val hostingName: String

	open fun parseVideoId(url: String?): String? {
		url ?: return null

		return idPattern.getGroupValue(url, 1)
	}

	open fun checkHostAffiliation(url: String?): Boolean {
		url ?: return false

		return url.matches(pattern.toRegex())
	}

	abstract fun getInfoUrl(incomingUrl: String?): String?

	abstract fun getPlayLink(videoId: String): String
}