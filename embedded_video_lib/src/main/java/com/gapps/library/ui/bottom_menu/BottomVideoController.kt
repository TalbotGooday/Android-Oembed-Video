package com.gapps.library.ui.bottom_menu

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import com.gapps.library.R
import com.gapps.library.ui.bottom_dialog.BottomSheetDialogFixed
import com.gapps.library.utils.getHeightFromWidth
import com.gapps.library.utils.getWidth

class BottomVideoController private constructor(
    private val context: Context?,
    private val listener: Listener?,
    @ColorInt private val titleColor: Int,
    @ColorInt private val textColor: Int,
    @ColorInt private val backgroundColor: Int,
    private val url: String?,
    private val titleText: String?,
    private val hostText: String?,
    private val playLink: String?,
    private val size: Pair<Float, Float>?,
    private val progressView: View?,
    private val isBottomControlPanelVisible: Boolean,
    @DrawableRes private val centerButtonIcon: Int,
    @StringRes private val leftButtonText: Int,
    @StringRes private val rightButtonText: Int,
    @ColorInt private val leftButtonTextColor: Int,
    @ColorInt private val rightButtonTextColor: Int,
    @ColorInt private val centerButtonIconTint: Int
) {
    companion object {
        var isVisible = false

        inline fun build(context: Context?, block: Builder.() -> Unit) =
            Builder(context).apply(block).build()
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
        builder.playLink,
        builder.size,
        builder.progressView,
        builder.isBottomControlPanelVisible,
        builder.centerButtonIcon,
        builder.leftButtonText,
        builder.rightButtonText,
        builder.leftButtonTextColor,
        builder.rightButtonTextColor,
        builder.centerButtonIconTint
    )

    @SuppressLint("InflateParams", "SetJavaScriptEnabled")
    fun show() {
        if (isVisible) {
            return
        }

        context ?: return

        val url = url ?: return

        Log.i("Player link", playLink ?: "none")

        val bottomSheetDialog = BottomSheetDialogFixed(context)
        val menuView = LayoutInflater.from(context).inflate(R.layout.layout_hc_video_view, null)
        val menuContainer = menuView.findViewById<LinearLayout>(R.id.vna_menu_container)
        val videoView = menuView.findViewById<WebView>(R.id.vna_video_view)
        val videoContainer = menuView.findViewById<FrameLayout>(R.id.vna_video_container)
        val progressBarContainer =
            menuView.findViewById<ViewGroup>(R.id.vna_video_progress_container)
        val title = menuView.findViewById<TextView>(R.id.vna_text_url_preview_title)
        val videoServiceType = menuView.findViewById<TextView>(R.id.vna_player_type)
        val leftButton = menuView.findViewById<TextView>(R.id.vna_left_button)
        val rightButton = menuView.findViewById<TextView>(R.id.vna_right_button)
        val centerButton = menuView.findViewById<AppCompatImageButton>(R.id.vna_center_button)
        val controlPanelOutline = menuView.findViewById<View>(R.id.vna_control_panel_outline)
        val controlPanel = menuView.findViewById<View>(R.id.vna_bottom_control_panel)

        controlPanel.visibility = if (isBottomControlPanelVisible) View.VISIBLE else View.GONE

        title.apply {
            this.setTextColor(titleColor)
            this.text = titleText
        }

        videoServiceType.apply {
            this.setTextColor(textColor)
            this.text = hostText
        }

        leftButton.apply {
            this.setTextColor(leftButtonTextColor)
            this.setText(leftButtonText)

            this.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
        }

        rightButton.apply {
            this.setTextColor(rightButtonTextColor)
            this.setText(rightButtonText)

            this.setOnClickListener {
                listener?.openLinkIn(url)

                bottomSheetDialog.dismiss()
            }
        }

        centerButton.apply {
            this.setImageResource(centerButtonIcon)
            this.setColorFilter(
                centerButtonIconTint,
                PorterDuff.Mode.SRC_IN
            )

            this.setOnClickListener {
                listener?.copyLink(url)
            }
        }

        menuContainer.apply {
            this.setBackgroundColor(backgroundColor)
        }

        val outlineColor = ColorUtils.setAlphaComponent(textColor, (255 * .1).toInt())
        controlPanelOutline.background.colorFilter =
            PorterDuffColorFilter(outlineColor, PorterDuff.Mode.SRC_IN)

        val videoViewWidth =
            context.getWidth(context.resources.getDimensionPixelSize(R.dimen.vna_bv_dialog_width))
        val videoViewHeight = getHeightFromWidth(size?.first, size?.second, videoViewWidth)

        if (progressView != null) {
            progressBarContainer.removeAllViews()
            progressBarContainer.addView(progressView)
        }

        videoContainer.apply {
            layoutParams.apply {
                this.width = videoViewWidth
                this.height = videoViewHeight
            }
        }

        videoView.apply {
            setBackgroundColor(backgroundColor)

            layoutParams.apply {
                this.width = videoViewWidth
                this.height = videoViewHeight
            }

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    return true
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    progressBarContainer.visibility = View.VISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    progressBarContainer.visibility = View.GONE
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun getDefaultVideoPoster(): Bitmap? {
                    return if (super.getDefaultVideoPoster() == null) {
                        try {
                            BitmapFactory.decodeResource(
                                context.resources,
                                R.drawable.ic_vna_play_icon
                            )
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

        if (playLink != null) {
            videoView.loadUrl(playLink)
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


        ViewCompat.setOnApplyWindowInsetsListener(menuContainer) { view, insets ->
            view?.apply {
                setPadding(paddingLeft, paddingTop, paddingRight, insets.systemWindowInsetBottom)
            }

            insets
        }
    }

    abstract class Listener {
        open fun openLinkIn(link: String) {}
        open fun copyLink(link: String) {}
    }

    class Builder(val context: Context?) {
        var listener: Listener? = null
            private set

        @ColorInt
        var titleColor = 0x1d1d26
            private set

        @ColorInt
        var textColor = Color.parseColor("#80000000")
            private set

        @ColorInt
        var backgroundColor = Color.WHITE
            private set

        var isBottomControlPanelVisible: Boolean = true
            private set

        var url: String? = null
            private set

        var titleText: String? = null
            private set

        var hostText: String? = null
            private set

        var playLink: String? = null
            private set

        var size: Pair<Float, Float>? = null
            private set

        var progressView: View? = null
            private set

        @DrawableRes
        var centerButtonIcon = R.drawable.ic_vna_content_copy
            private set

        @ColorInt
        var centerButtonIconTint = 0x1d1d26
            private set

        @ColorInt
        var rightButtonTextColor = 0x1d1d26
            private set

        @ColorInt
        var leftButtonTextColor = 0x1d1d26
            private set

        @StringRes
        var rightButtonText = R.string.vna_open_in
            private set

        @StringRes
        var leftButtonText = R.string.vna_close
            private set

        fun setVideoUrl(url: String?) = apply { this.url = url }
        fun setTitle(title: String?) = apply { this.titleText = title }
        fun setHostText(host: String?) = apply { this.hostText = host }
        fun setPlayLink(url: String?) = apply { this.playLink = url }
        fun setSize(width: Int, height: Int) =
            apply { this.size = width.toFloat() to height.toFloat() }

        fun setProgressView(view: View) = apply { this.progressView = view }

        fun setListener(listener: Listener) = apply { this.listener = listener }

        //Theme
        fun setTitleColor(@ColorInt color: Int) = apply { this.titleColor = color }
        fun setTextColor(@ColorInt color: Int) = apply { this.textColor = color }
        fun setLeftButtonTextColor(@ColorInt color: Int) =
            apply { this.leftButtonTextColor = color }

        fun setRightButtonTextColor(@ColorInt color: Int) =
            apply { this.rightButtonTextColor = color }

        fun setBackgroundColor(@ColorInt color: Int) = apply { this.backgroundColor = color }
        fun setCenterButtonIconTint(@ColorInt color: Int) =
            apply { this.centerButtonIconTint = color }

        fun setCenterButtonIcon(@DrawableRes resource: Int) =
            apply { this.centerButtonIcon = resource }

        fun setLeftButtonText(@StringRes resource: Int) = apply { this.leftButtonText = resource }
        fun setRightButtonText(@StringRes resource: Int) = apply { this.rightButtonText = resource }
        fun setBottomControlPanelVisible(isVisible: Boolean) =
            apply { this.isBottomControlPanelVisible = isVisible }

        fun build() = BottomVideoController(this)

        fun show() = BottomVideoController(this).show()
    }
}