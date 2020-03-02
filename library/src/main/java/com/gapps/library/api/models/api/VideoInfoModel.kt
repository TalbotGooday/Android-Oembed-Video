package com.gapps.library.api.models.api

interface VideoInfoModel<T> {
	val pattern: String
	val idPattern: String
	val type: Class<T>

	/**
	 *
	 */
	fun parseVideoId(url: String?): String?

	fun checkHostAffiliation(url: String?): Boolean
}