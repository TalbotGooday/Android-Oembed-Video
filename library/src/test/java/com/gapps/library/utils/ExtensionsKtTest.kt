package com.gapps.library.utils

import com.gapps.library.api.VideoService
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ExtensionsKtTest {
	@Before
	fun init() {
		VideoService.build {
			httpClient(okHttpClient)
			enableCache(true)
			enableLog(true)
		}
	}

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
	fun `Check url is a video link`() {
		for (url in videoUrls) {
			println(url)
			assertTrue(url.isVideoUrl())
		}

		for (url in nonVideoUrls) {
			println(url)
			assertFalse(url.isVideoUrl())
		}
	}

	@Test
	fun `Check text contains links`() {
		val text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, https://www.youtube.com/watch?v=M4BSGZ07NNA,\n" +
				"https://music.youtube.com/watch?v=lFMOYjVCLUo," +
				"https://vimeo.com/333257472," +
				"https://rutube.ru/video/d70e62b44b8893e98e3e90a6e2c9fcd4/?pl_type=source&amp;pl_id=18265,\n" +
				"https://www.facebook.com/UFC/videos/410056389868335/,\n" +
				"https://www.dailymotion.com/video/x5sxbmb,\n" +
				"https://dave.wistia.com/medias/0k5h1g1chs/,\n" +
				"https://vzaar.com/videos/401431sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."

		val videos = text.findVideos()

		assertEquals(videos.size, 8)
	}
}