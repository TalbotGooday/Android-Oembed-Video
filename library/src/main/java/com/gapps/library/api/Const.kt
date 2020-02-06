package com.gapps.library.api

//Patterns
//YouTube: https://regex101.com/r/nJzgG0/1
const val YOUTUBE_PATTERN = "(?:http(?:s)?:\\/\\/)?(?:www.)?(?:m.)?(?:music.)?youtu(?:be|.be)?(?:\\.com)?(?:(?:\\w*.?:\\/\\/)?\\w*.?\\w*-?.?\\w*\\/(?:embed|e|v|watch|.*\\/)?\\??(?:feature=\\w*\\.?\\w*)?&?(?:v=)?\\/?)([\\w\\d_-]{11})(?:\\S+)?"
//Vimeo: https://regex101.com/r/Nru4zu/1
const val VIMEO_PATTERN = "(?:http[s]?://)(?:w{3})?(?:player\\.)?vimeo\\.com/(?:[a-z]*/)*([0-9]{6,11})[?]?"
const val RUTUBE_PATTERN = "(?:http[s]?://)(?:w{3})?(?:player\\.)?rutube\\.ru/video/(?:embed/)?([A-Za-z0-9]+)[/]?(\\?.+)?"
//Facebook: https://regex101.com/r/98Nfkr/5
const val FACEBOOK_PATTERN = "(?:http[s]?://)?(?:www.|web.|m.)?(?:facebook|fb)?.com/(?:(?:video.php|watch?/)?\\?v=|.+/videos(?:/.+)?/)(\\d+)\\S*"
const val DAILYMOTION_PATTERN = "(?:http[s]?://)?(?:www\\.)?(?:(?:dailymotion\\.com(?:/embed)?/video)|dai\\.ly)/([a-zA-Z0-9]+)(?:_[\\w_-]+)?"
const val WISTIA_PATTERN = "(?:http[s]?:\\/\\/)?(?:.+)?(?:wistia\\.(?:com|net)|wi\\.st)\\/(?:medias|embed|series)\\/(?:iframe\\/?)?(?:\\S+\\?\\S*wvideoid=)?([a-zA-Z0-9]+)\\S*"
const val VZAAR_PATTERN = "(?:http[s]?://)?(?:.+)?vzaar.com/?(?:videos/)?([a-zA-Z0-9]+)\\S*"
//Hulu: https://regex101.com/r/LORZgZ/2
const val HULU_PATTERN = "(?:http[s]?:\\/\\/)?(?:www.)?hulu\\.(?:(?:com\\/\\S*(?:w(?:atch)?|eid)(?:\\/|=)?)|(?:tv\\/))?([a-zA-Z0-9]+)\\S*"
//Ustream: https://regex101.com/r/E0PMAV/2
const val USTREAM_PATTERN = "(?:http[s]?:\\/\\/)?(?:www\\.)?ustream.(?:com|tv)\\/(?:recorded|embed|channel)\\/?(?:([0-9]+)|(\\S+))(?:\\/\\S*)?"
//Ted: https://regex101.com/r/Cbhu4d/2
const val TED_TALKS_PATTERN = "(?:http[s]?:\\/\\/)?(?:www|embed)?\\.?ted\\.com\\/talks\\/([_a-zA-Z0-9]+)\\S*"

//Base
private const val YOUTUBE_BASE_URL = "https://www.youtube.com"
private const val VIMEO_BASE_URL = "http://vimeo.com"
private const val FACEBOOK_BASE_URL = "https://apps.facebook.com"
private const val DAILYMOTION_BASE_URL = "https://www.dailymotion.com"
private const val WISTIA_BASE_URL = "https://fast.wistia.net"
private const val RUTUBE_BASE_URL = "http://rutube.ru/api"
private const val VZAAR_BASE_URL = "https://app.vzaar.com/"
private const val HULU_BASE_URL = "https://www.hulu.com/"
private const val USTREAM_BASE_URL = "https://video.ibm.com/"
private const val TED_TALKS_BASE_URL = "https://www.ted.com/"

//Info
private const val OEMBED_INFO = "/oembed"
private const val VIMEO_INFO = "/api/v2/video/"
private const val FACEBOOK_INFO = "/plugins/video/oembed"
private const val DAILYMOTION_INFO = "/services/oembed/?url=https://www.dailymotion.com/video/"
private const val WISTIA_INFO = "/oembed?url="
private const val USTREAM_INFO = "/oembed?url="
private const val TED_TALKS_INFO = "services/v1/oembed.json?url="
private const val VZAAR_INFO = "/videos/"
private const val HULU_INFO = "api/oembed.json?url="
private const val FACEBOOK_VIDEOS = "?url=https://www.facebook.com/facebook/videos/"
private const val FORMAT = "format"
private const val FORMAT_JSON = "json"
private const val URL = "url"

fun String.getYoutubeInfoUrl(): String {
	val id = YOUTUBE_PATTERN.toRegex().find(this)?.groups?.get(1)?.value
	val url = "$YOUTUBE_BASE_URL/watch?v=$id"
	return "$YOUTUBE_BASE_URL$OEMBED_INFO?$FORMAT=$FORMAT_JSON&$URL=$url"

}

fun String.getVimeoInfoUrl(): String {
	val id = VIMEO_PATTERN.toRegex().find(this)?.groups?.get(1)?.value
	return "$VIMEO_BASE_URL$VIMEO_INFO$id.$FORMAT_JSON"
}


fun String.getFacebookInfoUrl(): String {
	val id = FACEBOOK_PATTERN.toRegex().find(this)?.groups?.get(1)?.value
	return "$FACEBOOK_BASE_URL$FACEBOOK_INFO.$FORMAT_JSON$FACEBOOK_VIDEOS$id"
}

fun String.getHuluInfoUrl(): String {
	val id = HULU_PATTERN.toRegex().find(this)?.groups?.get(1)?.value
	val url = "http://www.hulu.com/watch/$id"
	return "$HULU_BASE_URL$HULU_INFO$url"
}

fun String.getDailymotionInfoUrl(): String {
	val id = DAILYMOTION_PATTERN.toRegex().find(this)?.groups?.get(1)?.value
	return "$DAILYMOTION_BASE_URL$DAILYMOTION_INFO$id"
}

fun String.getTedTalksInfoUrl(): String {
	return "$TED_TALKS_BASE_URL$TED_TALKS_INFO$this"
}

fun String.getWistiaInfoUrl(): String {
	return "$WISTIA_BASE_URL$WISTIA_INFO$this"
}

fun String.getUstreamInfoUrl(): String {
	val id = USTREAM_PATTERN.toRegex().find(this)?.groups?.get(1)?.value
	val channelId = USTREAM_PATTERN.toRegex().find(this)?.groups?.get(2)?.value

	val url = if (id == null || this.contains("channel")) {
		"https://ustream.tv/channel/${channelId ?: id}"
	} else {
		"https://ustream.tv/recorded/$id"
	}

	return "$USTREAM_BASE_URL$USTREAM_INFO$url"
}

fun String.getVzaarInfoUrl(): String {
	val id = VZAAR_PATTERN.toRegex().find(this)?.groups?.get(1)?.value

	return "$VZAAR_BASE_URL$VZAAR_INFO$id.$FORMAT_JSON"
}


fun String.getRutubeInfoUrl(): String {
	val id = RUTUBE_PATTERN.toRegex().find(this)?.groups?.get(1)?.value ?: ""

	return if (id.length < 32) {
		"$RUTUBE_BASE_URL$OEMBED_INFO?$FORMAT=$FORMAT_JSON&$URL=$this"
	} else {
		"$RUTUBE_BASE_URL/video/$id/"
	}
}