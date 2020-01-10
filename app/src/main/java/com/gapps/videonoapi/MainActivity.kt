package com.gapps.videonoapi

import android.content.*
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.gapps.library.api.VideoService
import com.gapps.library.ui.bottom_menu.BottomVideoController
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

	private lateinit var videoService: VideoService

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		initService()

		youtube_container.setOnClickListener { getPreview(it) }
		youtube_music_container.setOnClickListener { getPreview(it) }
		vimeo_container.setOnClickListener { getPreview(it) }
		rutube_container.setOnClickListener { getPreview(it) }
		facebook_container.setOnClickListener { getPreview(it) }
		dailymotion_container.setOnClickListener { getPreview(it) }
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

	private fun getPreview(it: View?) {
		progress.visibility = View.VISIBLE
		val textView = getTextView(it) ?: return
		val url = textView.text?.toString() ?: return

		videoService.loadVideoPreview(url) { model ->
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
					.setSize(model.width, model.height)
					.setTitle(title)
					.setVideoUrl(initUrl)
					.show()

			progress.visibility = View.INVISIBLE
		}
	}

	private fun getTextView(it: View?): TextView? {
		it ?: return null

		if (it !is ViewGroup) return null

		for (i: Int in 0 until it.childCount) {
			val childAt = it.getChildAt(i)
			if (childAt is TextView) return childAt
		}

		return null
	}

	private fun copyLinkToClipboard(link: String) {
		val clip = ClipData.newPlainText("VideoUrl", link)
		(getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?.setPrimaryClip(clip)
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
