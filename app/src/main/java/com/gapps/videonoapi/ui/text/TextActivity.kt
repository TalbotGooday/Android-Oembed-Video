package com.gapps.videonoapi.ui.text

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import com.gapps.library.api.VideoService
import com.gapps.library.utils.findVideos
import com.gapps.library.utils.patterns.PatternVideoLinksBuilder
import com.gapps.videonoapi.databinding.ActivityTextBinding
import com.gapps.videonoapi.ui.base.BaseActivity
import com.gapps.videonoapi.video_utils.ultimedia.UltimediaVideoInfoModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit


class TextActivity : BaseActivity() {

    private lateinit var binding: ActivityTextBinding
    private lateinit var videoService: VideoService

    private val textString =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, https://www.youtube.com/watch?v=M4BSGZ07NNA,\n" +
                "https://music.youtube.com/watch?v=lFMOYjVCLUo," +
                "https://vimeo.com/333257472," +
                "https://streamable.com/s0phr," +
                "https://rutube.ru/video/d70e62b44b8893e98e3e90a6e2c9fcd4/?pl_type=source&amp;pl_id=18265,\n" +
                "https://www.facebook.com/UFC/videos/410056389868335/,\n" +
                "https://www.dailymotion.com/video/x5sxbmb,\n" +
                "https://dave.wistia.com/medias/0k5h1g1chs/,\n" +
                "https://vzaar.com/videos/401431sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            with(this@TextActivity)
            httpClient(okHttpClient)
            enableCache(true)
            enableLog(true)
            withCustomVideoInfoModels(UltimediaVideoInfoModel())
        }
    }

    private fun initViews() {
        initTextView(false)

        binding.clearLinks.setOnCheckedChangeListener { _, isChecked ->
            initTextView(isChecked)
        }
    }

    private fun initTextView(clearLinks: Boolean) {
        val spannableString = SpannableString(textString)

        val onClick = { url: String ->
            loadVideoInfo(url)
        }

        textString.findVideos(clearLinks).forEach {
            spannableString.setSpan(
                VideoUrlSpan(it, onClick),
                it.range.first,
                it.range.last,
                Spannable.SPAN_COMPOSING
            )
        }

        binding.text.run {
            text = spannableString
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
        }
    }

    private fun loadVideoInfo(url: String) {
        videoService.loadVideoPreview(url,
            { video ->
                showVideo(video)
            },
            { _, error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            })
    }

    private class VideoUrlSpan(
        private val item: PatternVideoLinksBuilder.VideoPatternItem,
        private val click: (String) -> Unit
    ) : ClickableSpan() {
        override fun onClick(widget: View) {
            click.invoke(item.url)
        }
    }
}
