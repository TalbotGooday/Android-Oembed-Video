package com.gapps.videonoapi

import android.content.*
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gapps.library.api.VideoService
import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.ui.bottom_menu.BottomVideoController
import com.gapps.videonoapi.adapters.VideoAdapter
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

	private lateinit var videoService: VideoService

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

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		initService()

		initViews()
	}

	private fun initViews() {
		videos_list.apply {
			layoutManager = LinearLayoutManager(this@MainActivity)
			adapter = VideoAdapter(videoService, object : VideoAdapter.Listener {
				override fun onItemClick(item: VideoPreviewModel) {
					showVideo(item)
				}
			}).apply {
				swapData(videoUrls)
			}

			addOnScrollListener(object : RecyclerView.OnScrollListener() {
				override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
					super.onScrolled(recyclerView, dx, dy)

					val isBottom = recyclerView.canScrollVertically(1).not()

					buttons_container.alpha = if (isBottom) .1f else 1f
				}
			})
		}

		refresh.setOnClickListener {
			(videos_list.adapter as VideoAdapter).swapData(videoUrls)
		}

		collapse_all.setOnClickListener {
			(videos_list.adapter as VideoAdapter).collapseAll()
		}
	}

	private fun initService() {
		val interceptor = HttpLoggingInterceptor()
		interceptor.level = HttpLoggingInterceptor.Level.BODY

		val okHttpClient = OkHttpClient.Builder()
				.connectTimeout(15, TimeUnit.SECONDS)
				.readTimeout(15, TimeUnit.SECONDS)
				.addInterceptor(interceptor)
				.build()

		videoService = VideoService.Builder()
				.httpClient(okHttpClient)
				.build()
	}

	private fun showVideo(model: VideoPreviewModel) {
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
			show()
		}

	}

	private fun copyLinkToClipboard(link: String) {
		val clip = ClipData.newPlainText("VideoUrl", link)
		(getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?.setPrimaryClip(clip)
		Toast.makeText(this, "Copied: $link", Toast.LENGTH_SHORT).show()
	}

	private fun openLink(link: String) {
		try {
			val intent = Intent(Intent.ACTION_VIEW).apply {
				data = Uri.parse(link)
			}

			startActivity(intent)
		} catch (e: ActivityNotFoundException) {
			e.printStackTrace()
		}
	}
}
