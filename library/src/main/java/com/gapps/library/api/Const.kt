package com.gapps.library.api

const val YOUTUBE_PATTERN = "(http(s)?:\\/\\/)?((w){3}.)?(m.)?(music.)?youtu(be|.be)?(\\.com)?\\/.*"
const val YOUTUBE_PATTERN_ID = "(?:(?:\\w*.?://)?\\w*.?\\w*-?.?\\w*/(?:embed|e|v|watch|.*/)?\\??(?:feature=\\w*\\.?\\w*)?&?(?:v=)?/?)([\\w\\d_-]+).*"
const val VIMEO_PATTERN = "(?:http[s]?:\\/\\/)(?:w{3})?(?:player\\.)?vimeo\\.com\\/(?:[a-z]*\\/)*([0-9]{6,11})[?]?.*"
const val RUTUBE_PATTERN = "(?:http[s]?:\\/\\/)(?:w{3})?(?:player\\.)?rutube\\.ru\\/video\\/(?:embed\\/)?([A-Za-z0-9]+)[\\/]?(\\?.+)?"
const val FACEBOOK_PATTERN = "(?:http[s]?:\\/\\/)?(?:www.|web.|m.)?(?:facebook|fb)?.com\\/(?:(?:video.php|watch?\\/)?\\?v=|.+\\/videos(?:\\/.+)?\\/)(\\d+)\\S*"
const val OEMBED_INFO = "/oembed"
const val VIMEO_INFO = "/api/v2/video/"
const val FACEBOOK_INFO = "/plugins/video/oembed"
const val FACEBOOK_VIDEOS = "?url=https://www.facebook.com/facebook/videos/"
const val YOUTUBE_BASE_URL = "https://www.youtube.com"
const val VIMEO_BASE_URL = "http://vimeo.com"
const val FACEBOOK_BASE_URL = "https://www.facebook.com"
const val RUTUBE_BASE_URL = "http://rutube.ru/api"
const val FORMAT = "format"
const val FORMAT_JSON = "json"
const val URL = "url"


fun String.getYoutubeInfoUrl(): String {
	return "$YOUTUBE_BASE_URL$OEMBED_INFO?$FORMAT=$FORMAT_JSON&$URL=$this"

}

fun String.getVimeoInfoUrl(): String {
	val id = VIMEO_PATTERN.toRegex().find(this)?.groups?.get(1)?.value
	return "$VIMEO_BASE_URL$VIMEO_INFO$id.$FORMAT_JSON"
}


fun String.getFacebookInfoUrl(): String {
	val id = FACEBOOK_PATTERN.toRegex().find(this)?.groups?.get(1)?.value
	return "$FACEBOOK_BASE_URL$FACEBOOK_INFO$FORMAT_JSON$FACEBOOK_VIDEOS$id"
}


fun String.getRutubeInfoUrl(): String {
	val id = RUTUBE_PATTERN.toRegex().find(this)?.groups?.get(1)?.value ?: ""

	return if (id.length < 32) {
		"$RUTUBE_BASE_URL$OEMBED_INFO?$FORMAT=$FORMAT_JSON&$URL=$this"
	} else {
		"$RUTUBE_BASE_URL/video/$id/"
	}
}