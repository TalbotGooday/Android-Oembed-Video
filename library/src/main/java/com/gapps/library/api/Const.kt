package com.gapps.library.api

import com.gapps.library.utils.getGroupValue

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
//Coub: https://regex101.com/r/ZoQVLa/1
const val COUB_PATTERN = "(?:http[s]?:\\/\\/)?(?:www)?\\.?coub\\.com\\/(?:embed|view|api)\\/([_a-zA-Z0-9]+)\\S*"

//Base
private const val VIMEO_BASE_URL = "http://vimeo.com"
private const val FACEBOOK_BASE_URL = "https://apps.facebook.com"
private const val DAILYMOTION_BASE_URL = "https://www.dailymotion.com"
private const val WISTIA_BASE_URL = "https://fast.wistia.net"
private const val RUTUBE_BASE_URL = "http://rutube.ru/api"
private const val VZAAR_BASE_URL = "https://app.vzaar.com/"
private const val HULU_BASE_URL = "https://www.hulu.com"
private const val USTREAM_BASE_URL = "https://video.ibm.com/"
private const val TED_TALKS_BASE_URL = "https://www.ted.com"
private const val COUB_BASE_URL = "http://coub.com/"

//Info
private const val FACEBOOK_VIDEOS = "https://www.facebook.com/facebook/videos/"
private const val DAILYMOTION_VIDEOS = "https://www.dailymotion.com/video/"
private const val HULU_VIDEOS = "http://www.hulu.com/watch/"
private const val YOUTUBE_VIDEOS = "https://www.youtube.com/watch?v="
private const val FORMAT = "format"
private const val FORMAT_JSON = "json"
private const val URL = "url"

fun String.getYoutubeInfoUrl(): String {
	val id = YOUTUBE_PATTERN.getGroupValue(this, 1)
	return "https://www.youtube.com/oembed?$FORMAT=$FORMAT_JSON&$URL=$YOUTUBE_VIDEOS$id"
}

fun String.getVimeoInfoUrl(): String {
	val id = VIMEO_PATTERN.getGroupValue(this, 1)
	return "$VIMEO_BASE_URL/api/v2/video/$id.$FORMAT_JSON"
}

fun String.getFacebookInfoUrl(): String {
	val id = FACEBOOK_PATTERN.getGroupValue(this, 1)
	return "$FACEBOOK_BASE_URL/plugins/video/oembed.$FORMAT_JSON?$URL=$FACEBOOK_VIDEOS$id"
}

fun String.getHuluInfoUrl(): String {
	val id = HULU_PATTERN.getGroupValue(this, 1)
	return "$HULU_BASE_URL/api/oembed.$FORMAT=$FORMAT_JSON&$URL=$HULU_VIDEOS$id"
}

fun String.getDailymotionInfoUrl(): String {
	val id = DAILYMOTION_PATTERN.getGroupValue(this, 1)
	return "$DAILYMOTION_BASE_URL/services/oembed/?$URL=$DAILYMOTION_VIDEOS$id"
}

fun String.getTedTalksInfoUrl(): String {
	return "$TED_TALKS_BASE_URL/services/v1/oembed.$FORMAT_JSON?$URL=$this"
}

fun String.getCoubInfoUrl(): String {
	return "$COUB_BASE_URL/api/oembed.$FORMAT_JSON?$URL=$this"
}

fun String.getWistiaInfoUrl(): String {
	return "$WISTIA_BASE_URL/oembed?$URL=$this"
}

fun String.getUstreamInfoUrl(): String {
	val id = USTREAM_PATTERN.getGroupValue(this, 1)
	val channelId = USTREAM_PATTERN.getGroupValue(this, 2)

	val url = if (id == null || this.contains("channel")) {
		"https://ustream.tv/channel/${channelId ?: id}"
	} else {
		"https://ustream.tv/recorded/$id"
	}

	return "$USTREAM_BASE_URL/oembed?$URL=$url"
}

fun String.getVzaarInfoUrl(): String {
	val id = VZAAR_PATTERN.getGroupValue(this, 1)

	return "$VZAAR_BASE_URL/videos/$id.$FORMAT_JSON"
}

fun String.getRutubeInfoUrl(): String {
	val id = RUTUBE_PATTERN.getGroupValue(this, 1) ?: ""

	return if (id.length < 32) {
		"$RUTUBE_BASE_URL/oembed?$FORMAT=$FORMAT_JSON&$URL=$this"
	} else {
		"$RUTUBE_BASE_URL/video/$id/"
	}
}