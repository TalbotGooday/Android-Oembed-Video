
# Android-Oembed-Video
A simple library for parsing and playing links from YouTube, YouTube Music, Vimeo and Rutube is WebView without the need to connect api data services

|<img src="/screenshots/video_no_api2.jpg"/> | <img src="/screenshots/video_no_api1.jpg"/> |

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
