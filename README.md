
# Android Oembed Video
A simple library for parsing and playing links from YouTube, YouTube Music, Vimeo and Rutube and others in the WebView without the need to connect data API services.

[![Release](https://jitpack.io/v/TalbotGooday/Android-Oembed-Video.svg)](https://jitpack.io/#TalbotGooday/Android-Oembed-Video) [![Platform](https://img.shields.io/badge/platforms-Android-green.svg)]() [![Languages](https://img.shields.io/badge/languages-Kotlin-F18E33.svg)]()

## Supported Video Hostings

* <img src="https://github.com/TalbotGooday/Android-Oembed-Video/blob/master/app/src/main/res/drawable-xxhdpi/youtube.png" width=18px/>    YouTube

* <img src="https://github.com/TalbotGooday/Android-Oembed-Video/blob/master/app/src/main/res/drawable-xxhdpi/youtube_music.png" width=18px/> YouTube Music

* <img src="https://github.com/TalbotGooday/Android-Oembed-Video/blob/master/app/src/main/res/drawable-xxhdpi/vimeo.png" width=18px/> Vimeo

* <img src="https://github.com/TalbotGooday/Android-Oembed-Video/blob/master/app/src/main/res/drawable-xxhdpi/rutube.png" width=18px/> Rutube

* <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/fb/Facebook_icon_2013.svg/1024px-Facebook_icon_2013.svg.png" width=18px/> Facebook (the thumbnail is not available due to api restrictions)
* <img src="https://github.com/TalbotGooday/Android-Oembed-Video/blob/master/app/src/main/res/drawable-xxhdpi/dailymotion.png" width=18px/> Dailymotion
* <img src="https://www.saashub.com/images/app/service_logos/25/1a1b2c9e8acc/large.png?1547934029" width=18px/> Wistia
* <img src="https://static.crozdesk.com/web_app_library/providers/logos/000/003/720/original/vzaar-1559230945-logo.png?1559230945" width=18px/> ~~Vzaar~~ (it's Dacast now) 
* <img src="https://github.com/TalbotGooday/Android-Oembed-Video/blob/master/app/src/main/res/drawable-xxhdpi/hulu.png" width=18px/> Hulu
* <img src="https://blog.video.ibm.com/wp-content/uploads/2014/10/U_logo_blue-2.png" width=18px/> Ustream
* <img src="https://github.com/TalbotGooday/Android-Oembed-Video/blob/master/app/src/main/res/drawable-xxhdpi/ted_talks.png" width=18px/> Ted Talks
* <img src="https://cdn.iconscout.com/icon/free/png-512/coub-1693601-1442642.png" width=18px/> Coub
* <img src="https://cdn.embed.ly/providers/logos/streamable.png" width=18px/> Streamable
* <img src="https://github.com/TalbotGooday/Android-Oembed-Video/blob/master/app/src/main/res/drawable-xxhdpi/loom.png" width=18px/> Loom

## Screenshots

<img src="/screenshots/device-2020-02-06-232720.png" width=32%/> <img src="/screenshots/device-2020-02-06-232746.png" width=32%/> <img src="/screenshots/device-2020-02-06-232924.png" width=32%/>

Add it in your root build.gradle at the end of repositories:
```java
allprojects {
        repositories {
                ...
                maven { url 'https://jitpack.io' }
        }
}
```
Add the dependency

```java
dependencies {
        implementation 'com.github.TalbotGooday:Android-Oembed-Video:Tag'
}

```

## Work Flow
1. Create your OkHttpClient and add it to the VideoService.Builder
```kotlin
val okHttpClient = OkHttpClient.Builder()
	.connectTimeout(15, TimeUnit.SECONDS)
	.readTimeout(15, TimeUnit.SECONDS)
	.build()

val videoService = VideoService.build{
	with(this@MainActivity)
	httpClient(okHttpClient)
	enableCache(true)
	enableLog(true)
}
```
2. Get VideoPreviewModel
```kotlin
videoService.loadVideoPreview(
	url,
	onSuccess = { video ->
		//handle a video model
	},
	onError = { url, error ->
		//handle an error
	})
```
3. Enable/disable caching
```kotlin
val videoService = VideoService.build {
	enableCache(true)
}
```
4. Enable/disable logging
```kotlin
val videoService = VideoService.build {
	enableLog(BuildConfig.DEBUG)
}
```
## Play Video from VideoPreviewModel
The BottomVideoController allows to run any oembed video in WebView.
```kotlin
val host = model.videoHosting
val linkToPlay = model.linkToPlay
val title = model.videoTitle
val initUrl = model.url

BottomVideoController.build(this) {
	setListener(object : BottomVideoController.Listener() {
		override fun openLinkIn(link: String) {
			openLink(link)
		}
		override fun copyLink(link: String) {
			copyLinkToClipboard(link)
		}
	})
	setHostText(host)
	setPlayLink(linkToPlay)
	setSize(model.width, model.height)
	setTitle(title)
	setVideoUrl(initUrl)
	setProgressView(TextView(this@MainActivity).apply { text = "Loading" })
	show()
}
```
## How to add some other video hosting
1. Add the `Gson` library to your project
2. Create the `Gson` data class from the embed response of the video service. Make this class a subclass of `VideoInfoModel`, implement the` toPreview` function, and override it:
```kotlin
 override fun toPreview(url: String?, linkToPlay: String, hostingName: String, videoId: String): VideoPreviewModel {
        return VideoPreviewModel(url, linkToPlay, hostingName, videoId).apply {
            this.thumbnailUrl = this@UltimediaResponse.thumbnailUrl
            this.videoTitle = this@UltimediaResponse.authorName
            this.width = this@UltimediaResponse.width.toInt()
            this.height = this@UltimediaResponse.height.toInt()
        }
    }
```
3. Create a subclass of `VideoInfoModel`, implement members and override them:
```kotlin
class UltimediaVideoInfoModel: VideoInfoModel<UltimediaResponse>() {
	override val baseUrl: String
		get() = "https://www.ultimedia.com"
	//https://regex101.com/r/2AsrOc/1
	override val pattern: String
		get() = "(?:http[s]?:\\/\\/)?(?:www)?\\.?ultimedia\\.com\\/(?:deliver|default|api)\\/.*\\/([_a-zA-Z0-9]+)\\S*"
	override val idPattern: String
		get() = pattern //or some another video id search pattern
	override val type: Class<UltimediaResponse>
		get() = UltimediaResponse::class.java
	override val hostingName: String
		get() = "Ultimedia"

	override fun getInfoUrl(incomingUrl: String?): String? {
		return "$baseUrl/api/search/oembed?$FORMAT=$FORMAT_JSON&$URL=$incomingUrl"
	}

	override fun getPlayLink(videoId: String): String {
		return "https://www.ultimedia.com/deliver/generic/iframe/src/$videoId/"
	}
}
``` 
**Note:** By default, the index of the `Regex` group should be **1**. If your `idPattern` does not fulfill this condition, then override the `parseVideoId` method:
```kotlin
override fun parseVideoId(url: String?): String? {
	url ?: return null
	return idPattern.toRegex().find(url)?.groups?.get(**someIndex**)?.value
}
```
## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details

