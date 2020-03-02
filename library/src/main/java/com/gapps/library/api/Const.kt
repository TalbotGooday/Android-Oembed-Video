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
//Coub: https://regex101.com/r/ZoQVLa/1
const val COUB_PATTERN = "(?:http[s]?:\\/\\/)?(?:www)?\\.?coub\\.com\\/(?:embed|view|api)\\/([_a-zA-Z0-9]+)\\S*"

const val FORMAT = "format"
const val FORMAT_JSON = "json"
const val URL = "url"
