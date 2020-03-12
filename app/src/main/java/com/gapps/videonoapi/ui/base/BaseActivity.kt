package com.gapps.videonoapi.ui.base

import android.content.*
import android.net.Uri
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.ui.bottom_menu.BottomVideoController
import com.gapps.videonoapi.R

abstract class BaseActivity : AppCompatActivity() {

	protected fun showVideo(model: VideoPreviewModel) {
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
			setBackgroundColor(R.color.colorBackground)
			setTextColor(R.color.colorHostName)
			setTitleColor(R.color.colorVideoTitle)
			setLeftButtonText(R.string.vna_close)
			setRightButtonText(R.string.vna_open_in)
			setRightButtonTextColor(R.color.colorVideoTitle)
			setLeftButtonTextColor(R.color.colorVideoTitle)
			setCenterButtonIcon(R.drawable.ic_vna_content_copy)
			setCenterButtonIconTint(R.color.colorVideoTitle)
			setProgressView(TextView(this@BaseActivity).apply { text = "Loading"; setTextColor(-1) })
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