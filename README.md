
# Android Oembed Video
A simple library for parsing and playing links from YouTube, YouTube Music, Vimeo and Rutube is WebView without the need to connect api data services

[![Release](https://jitpack.io/v/TalbotGooday/Android-Oembed-Video.svg)](https://jitpack.io/#TalbotGooday/Android-Oembed-Video)

# Supported Video Hostings

* <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/0/09/YouTube_full-color_icon_%282017%29.svg/1200px-YouTube_full-color_icon_%282017%29.svg.png" width=18px/>    YouTube

* <img src="https://icon-library.net/images/youtube-music-icon/youtube-music-icon-17.jpg" width=18px/> YouTube Music

* <img src="https://icon-library.net/images/vimeo-icon-vector/vimeo-icon-vector-4.jpg" width=18px/> Vimeo

* <img src="https://www.softrew.ru/uploads/posts/2016-12/1482058583_kak-skachat-video-s-rutuba.jpg" width=18px/> Rutube


# Screenshots

<img src="/screenshots/video_no_api2.jpg" width=40%/> <img src="/screenshots/video_no_api1.jpg" width=40%/>

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
