
# Android Oembed Video
A simple library for parsing and playing links from YouTube, YouTube Music, Vimeo and Rutube and others in the WebView without the need to connect data API services.

[![Release](https://jitpack.io/v/TalbotGooday/Android-Oembed-Video.svg)](https://jitpack.io/#TalbotGooday/Android-Oembed-Video)

# Supported Video Hostings

* <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/0/09/YouTube_full-color_icon_%282017%29.svg/1200px-YouTube_full-color_icon_%282017%29.svg.png" width=18px/>    YouTube

* <img src="https://icon-library.net/images/youtube-music-icon/youtube-music-icon-17.jpg" width=18px/> YouTube Music

* <img src="https://icon-library.net/images/vimeo-icon-vector/vimeo-icon-vector-4.jpg" width=18px/> Vimeo

* <img src="https://www.softrew.ru/uploads/posts/2016-12/1482058583_kak-skachat-video-s-rutuba.jpg" width=18px/> Rutube

* <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/fb/Facebook_icon_2013.svg/1024px-Facebook_icon_2013.svg.png" width=18px/> Facebook
* <img src="https://upload.wikimedia.org/wikipedia/commons/2/27/Logo_dailymotion.png" width=18px/> Dailymotion
* <img src="https://www.saashub.com/images/app/service_logos/25/1a1b2c9e8acc/large.png?1547934029" width=18px/> Wistia
* <img src="https://static.crozdesk.com/web_app_library/providers/logos/000/003/720/original/vzaar-1559230945-logo.png?1559230945" width=18px/> Vzaar
* <img src="https://www.hulu.com/static/favicon.ico.png" width=18px/> Hulu
* <img src="https://blog.video.ibm.com/wp-content/uploads/2014/10/U_logo_blue-2.png" width=18px/> Ustream
* <img src="https://github.com/TalbotGooday/Android-Oembed-Video/blob/master/app/src/main/res/drawable-xxhdpi/ted_talks.png" width=18px/> Ted Talks

# Screenshots

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

# Work Flow
1. Create your OkHttpClient and add it to the VideoService.Builder
```kotlin
val okHttpClient = OkHttpClient.Builder()
	.connectTimeout(15, TimeUnit.SECONDS)
	.readTimeout(15, TimeUnit.SECONDS)
	.build()

val videoService = VideoService.Builder()
	.httpClient(okHttpClient)
	.build()
```
2. Get VideoPreviewModel
```kotlin
videoService.loadVideoPreview(url) { model -> ... }
```
# Play Video from VideoPreviewModel
The BottomVideoController allows to run any oembed video in WebView.
```kotlin
val host = model.videoHosting
val linkToPlay = model.linkToPlay
val title = model.videoTitle
val initUrl = model.url

BottomVideoController.Builder(this)
	.setListener(object : BottomVideoController.Listener() {
		override fun openLinkIn(link: String) {
			openLink(link)
		}

		override fun copyLink(link: String) {
			copyLinkToClipboard(link)
		}
	})
	.setHostText(host)
	.setPlayLink(linkToPlay)
	.setTitle(title)
	.setVideoUrl(initUrl)
	.show()
```
