package com.gapps.library.ui.bottom_menu

import android.content.Context
import android.graphics.*
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.gapps.library.R
import com.google.android.material.bottomsheet.BottomSheetDialog

class BottomVideoController private constructor(
		private val context: Context?,
		private val listener: Listener?,
		@ColorRes private val titleColor: Int,
		@ColorRes private val textColor: Int,
		@ColorRes private val backgroundColor: Int,
		private val url: String?,
		private val titleText: String?,
		private val hostText: String?,
		private val playLink: String?
) {
	companion object {
		var isVisible = false

		inline fun build(context: Context?, block: Builder.() -> Unit) = Builder(context).apply(block).build()
	}

	private constructor(builder: Builder) : this(
			builder.context,
			builder.listener,
			builder.titleColor,
			builder.textColor,
			builder.backgroundColor,
			builder.url,
			builder.titleText,
			builder.hostText,
			builder.playLink
	)

	fun showBottomPopupMenu() {
		context ?: return

		val url = url ?: return

		val bottomSheetDialog = BottomSheetDialog(context)
		val menuView = LayoutInflater.from(context).inflate(R.layout.layout_hc_video_view, null)
		val menuContainer = menuView.findViewById<LinearLayout>(R.id.menu_container)
		val videoView = menuView.findViewById<WebView>(R.id.video_view)
		val progressBar = menuView.findViewById<ProgressBar>(R.id.video_progress)
		val title = menuView.findViewById<TextView>(R.id.text_url_preview_title)
		val videoServiceType = menuView.findViewById<TextView>(R.id.player_type)
		val closeVideo = menuView.findViewById<TextView>(R.id.close_video)
		val openVideoIn = menuView.findViewById<TextView>(R.id.open_video_in)
		val copyLink = menuView.findViewById<AppCompatImageButton>(R.id.copy_video_link)
		val controlPanelOutline = menuView.findViewById<View>(R.id.control_panel_outline)

		title.apply {
			this.setTextColor(ContextCompat.getColor(this.context, titleColor))
			this.text = titleText
		}

		val textColorInt = ContextCompat.getColor(this.context, textColor)

		videoServiceType.apply {
			this.setTextColor(textColorInt)
			this.text = hostText
		}

		closeVideo.apply {
			setOnClickListener {
				bottomSheetDialog.dismiss()
			}
			this.setTextColor(textColorInt)
		}

		openVideoIn.apply {
			setOnClickListener {
				listener?.openLinkIn(url)

				bottomSheetDialog.dismiss()
			}

			this.setTextColor(textColorInt)
		}

		copyLink.apply {
			setOnClickListener {
				listener?.copyLink(url)
			}

			this.setColorFilter(textColorInt, PorterDuff.Mode.SRC_IN)
		}

		menuContainer.apply {
			setBackgroundColor(ContextCompat.getColor(this.context, backgroundColor))
		}

		val outlineColor = ColorUtils.setAlphaComponent(textColorInt, (255 * .1).toInt())
		controlPanelOutline.background.colorFilter = PorterDuffColorFilter(outlineColor, PorterDuff.Mode.SRC_IN)

		videoView.apply {
			layoutParams.apply {
				val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
				val display = wm.defaultDisplay
				val size = Point()
				display.getSize(size)

				this.width = size.x
				this.height = size.x / 16 * 9
			}

			loadUrl(playLink)

			webViewClient = object : WebViewClient() {
				override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
					return true
				}

				override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
					super.onPageStarted(view, url, favicon)
					progressBar.visibility = View.VISIBLE
				}

				override fun onPageFinished(view: WebView?, url: String?) {
					super.onPageFinished(view, url)
					progressBar.visibility = View.GONE
				}
			}

			webChromeClient = object : WebChromeClient() {
				override fun getDefaultVideoPoster(): Bitmap? {
					return if (super.getDefaultVideoPoster() == null) {
						try {
							BitmapFactory.decodeResource(context.resources, R.drawable.ic_play_icon)
						} catch (e: Exception) {
							null
						}
					} else {
						super.getDefaultVideoPoster()
					}
				}
			}

			settings.apply {
				javaScriptEnabled = true
				domStorageEnabled = true
			}
		}

		bottomSheetDialog.apply {
			setContentView(menuView)

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
			}
			setOnShowListener {
				isVisible = true
			}
			setOnDismissListener {
				isVisible = false
			}
			show()
		}
	}

	abstract class Listener {
		open fun openLinkIn(link: String) {}
		open fun copyLink(link: String) {}
	}

	class Builder(val context: Context?) {
		var listener: Listener? = null
			private set

		@ColorRes
		var titleColor = R.color.color_video_title_text
			private set

		@ColorRes
		var textColor = R.color.color_video_title_text
			private set

		@ColorRes
		var backgroundColor = android.R.color.white
			private set

		var url: String? = null
			private set

		var titleText: String? = null
			private set

		var hostText: String? = null
			private set

		var playLink: String? = null
			private set

		fun setVideoUrl(url: String?) = apply { this.url = url }
		fun setTitle(title: String?) = apply { this.titleText = title }
		fun setHostText(host: String?) = apply { this.hostText = host }
		fun setPlayLink(url: String?) = apply { this.playLink = url }

		fun setListener(listener: Listener) = apply { this.listener = listener }
		fun setTitleColor(@ColorRes color: Int) = apply { this.titleColor = color }
		fun setTextColor(@ColorRes color: Int) = apply { this.textColor = color }
		fun setBackgroundColor(@ColorRes color: Int) = apply { this.backgroundColor = color }

		fun build() = BottomVideoController(this)

		fun show() = BottomVideoController(this).showBottomPopupMenu()
	}
}