package com.gapps.library.utils

import org.junit.Test

import org.junit.Assert.*

class ExtensionsKtTest {
	private val videoUrls = listOf(
			"https://www.youtube.com/watch?v=M4BSGZ07NNA",
			"https://music.youtube.com/watch?v=lFMOYjVCLUo",
			"https://vimeo.com/333257472",
			"https://rutube.ru/video/d70e62b44b8893e98e3e90a6e2c9fcd4/?pl_type=source&amp;pl_id=18265",
			"https://www.facebook.com/UFC/videos/410056389868335/",
			"https://www.dailymotion.com/video/x5sxbmb",
			"https://dave.wistia.com/medias/0k5h1g1chs/",
			"https://vzaar.com/videos/401431"
	)

	private val nonVideoUrls = listOf(
			"asdfasdfasdfasdf",
			"Some text",
			"https://www.facebook.com/",
			"https://vzaar.com/",
			"https://www.youtube.com/",
			"https://music.youtube.com/",
			"https://vimeo.com/",
			"https://rutube.ru/",
			"https://www.facebook.com/",
			"https://www.dailymotion.com/",
			"https://dave.wistia.com/",
			"https://vzaar.com/"
	)

	@Test
	fun `Check url is video link`() {
		for (url in videoUrls){
			println(url)
			assertTrue(url.isVideoUrl())
		}

		for (url in nonVideoUrls){
			println(url)
			assertFalse(url.isVideoUrl())
		}
	}
}