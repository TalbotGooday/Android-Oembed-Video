package com.gapps.videonoapi.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.gapps.library.api.VideoService
import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.utils.isVideoUrl
import com.gapps.videonoapi.databinding.ActivityMainBinding
import com.gapps.videonoapi.ui.base.BaseActivity
import com.gapps.videonoapi.ui.main.adapters.VideoAdapter
import com.gapps.videonoapi.ui.text.TextActivity
import com.gapps.videonoapi.utils.extensions.alphaSmooth
import com.gapps.videonoapi.utils.extensions.convertDpToPx
import com.gapps.videonoapi.utils.scroll.ScrollListener
import com.gapps.videonoapi.video_utils.ultimedia.UltimediaVideoInfoModel
import com.gapps.videonoapi.video_utils.youtube.MyYoutubeVideoInfoModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity() {

    private lateinit var videoService: VideoService

    private lateinit var binding: ActivityMainBinding

    private val videoUrls = listOf(
        "https://www.youtube.com/watch?v=M4BSGZ07NNA",
        "https://music.youtube.com/watch?v=lFMOYjVCLUo",
        "https://streamable.com/s0phr",
        "https://vimeo.com/259411563",
        "https://rutube.ru/video/d70e62b44b8893e98e3e90a6e2c9fcd4/?pl_type=source&amp;pl_id=18265",
        "https://www.facebook.com/watch?v=795751214848051",
        "https://www.dailymotion.com/video/x5sxbmb",
        "https://dave.wistia.com/medias/0k5h1g1chs/",
        "https://vzaar.com/videos/401431",
        "http://www.hulu.com/w/154323",
        "https://ustream.tv/channel/6540154",
        "https://ustream.tv/recorded/101541339",
        "https://www.ted.com/talks/jill_bolte_taylor_my_stroke_of_insight",
        "https://coub.com/view/um0um0",
        "https://www.ultimedia.com/default/index/videogeneric/id/pzkk35/",
        "https://loom.com/share/0281766fa2d04bb788eaf19e65135184",
        "https://notAVideoHost.tv/recorded/101541339",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
            true

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(
                insets.left,
                insets.top,
                insets.right,
                insets.bottom
            )

            // Return CONSUMED to prevent further propagation of insets to child views
            windowInsets
        }

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
            enableCache(false)
            enableLog(true)
            withCustomVideoInfoModels(UltimediaVideoInfoModel(), MyYoutubeVideoInfoModel())
        }
    }

    private fun initViews() = with(binding) {
        videosList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            val videoAdapter =
                VideoAdapter(
                    context = this@MainActivity,
                    videoService = videoService,
                    listener = object : VideoAdapter.Listener {
                        override fun onItemClick(item: VideoPreviewModel) {
                            showVideo(item)
                        }
                    }
                )

            adapter = videoAdapter.apply {
                swapData(getValidUrls())
            }

            addOnScrollListener(ScrollListener(convertDpToPx(100f)) {
                buttonsContainer.alphaSmooth(if (it) .1f else 1f)
            })
        }

        textTest.setOnClickListener {
            startActivity(Intent(this@MainActivity, TextActivity::class.java))
        }

        collapseAll.setOnClickListener {
            (videosList.adapter as VideoAdapter).collapseAll()
        }

        swiperefresh.setOnRefreshListener {
            (videosList.adapter as VideoAdapter).swapData(getValidUrls())
            swiperefresh.isRefreshing = false
        }
    }

    private fun getValidUrls() = videoUrls.filter { it.isVideoUrl() }
}
