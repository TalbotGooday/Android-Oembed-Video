package com.gapps.videonoapi.ui.main

import android.content.*
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gapps.library.api.VideoService
import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.utils.isVideoUrl
import com.gapps.videonoapi.R
import com.gapps.videonoapi.ui.text.TextActivity
import com.gapps.videonoapi.ui.main.adapters.VideoAdapter
import com.gapps.videonoapi.video_utils.abraira.UltimediaVideoInfoModel
import com.gapps.videonoapi.ui.base.BaseActivity
import com.gapps.videonoapi.utils.recycler_view.MarginItemDecoration
import com.gapps.videonoapi.utils.scroll.ScrollListener
import com.gapps.videonoapi.utils.extensions.alphaSmooth
import com.gapps.videonoapi.utils.extensions.convertDpToPx
import com.gapps.videonoapi.video_utils.youtube.MyYoutubeVideoInfoModel
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity() {

	private lateinit var videoService: VideoService

	private val videoUrls = listOf(
			"https://www.youtube.com/watch?v=M4BSGZ07NNA",
			"https://music.youtube.com/watch?v=lFMOYjVCLUo",
			"https://vimeo.com/259411563",
			"https://rutube.ru/video/d70e62b44b8893e98e3e90a6e2c9fcd4/?pl_type=source&amp;pl_id=18265",
			"https://www.facebook.com/kinodizi/videos/965220097161488",
			"https://www.dailymotion.com/video/x5sxbmb",
			"https://dave.wistia.com/medias/0k5h1g1chs/",
			"https://vzaar.com/videos/401431",
			"http://www.hulu.com/w/154323",
			"https://ustream.tv/channel/6540154",
			"https://ustream.tv/recorded/101541339",
			"https://www.ted.com/talks/jill_bolte_taylor_my_stroke_of_insight",
			"https://coub.com/view/um0um0",
			"https://www.ultimedia.com/default/index/videogeneric/id/pzkk35/",
			"https://notAVideoHost.tv/recorded/101541339"
	)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		initService()

		initViews()
	}

	private fun initService() {
		val interceptor = HttpLoggingInterceptor()
		interceptor.level = HttpLoggingInterceptor.Level.BODY

		val okHttpClient = OkHttpClient.Builder()
				.connectTimeout(15, TimeUnit.SECONDS)
				.readTimeout(15, TimeUnit.SECONDS)
				.addInterceptor(interceptor)
				.build()

		videoService = VideoService.build {
			with(this@MainActivity)
			httpClient(okHttpClient)
			enableCache(true)
			enableLog(false)
			withCustomVideoInfoModels(UltimediaVideoInfoModel(), MyYoutubeVideoInfoModel())
		}
	}

	private fun initViews() {
		videos_list.apply {
			layoutManager = LinearLayoutManager(this@MainActivity)
			val videoAdapter = VideoAdapter(this@MainActivity, videoService, object : VideoAdapter.Listener {
				override fun onItemClick(item: VideoPreviewModel) {
					showVideo(item)
				}
			})
			adapter = videoAdapter.apply {
				swapData(videoUrls.filter { it.isVideoUrl() })
			}

			addOnScrollListener(ScrollListener(convertDpToPx(100f)) {
				buttons_container.alphaSmooth(if (it) .1f else 1f)
			})

			addItemDecoration(MarginItemDecoration(videoAdapter, top = false, bottom = true))
		}

		text_test.setOnClickListener {
			startActivity(Intent(this, TextActivity::class.java))
		}

		collapse_all.setOnClickListener {
			(videos_list.adapter as VideoAdapter).collapseAll()
		}

		swiperefresh.setOnRefreshListener {
			(videos_list.adapter as VideoAdapter).swapData(videoUrls)
			swiperefresh.isRefreshing = false
		}
	}
}
