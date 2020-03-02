package com.gapps.library.api

import android.content.Context
import android.util.Log
import com.gapps.library.api.models.api.*
import com.gapps.library.api.models.video.VideoPreviewModel
import okhttp3.OkHttpClient


class VideoService(
		context: Context?,
		client: OkHttpClient,
		isCacheEnabled: Boolean,
		val isLogEnabled: Boolean
) {
	private val videoInfoModelsList = mutableListOf(
			CoubVideoInfoModel(),
			DailymotionVideoInfoModel(),
			FacebookVideoInfoModel(),
			HuluVideoInfoModel(),
			RutubeVideoInfoModel(),
			TedTalksVideoInfoModel(),
			UstreamVideoInfoModel(),
			VimeoVideoInfoModel(),
			VzaarVideoInfoModel(),
			WistiaVideoInfoModel(),
			YoutubeMusicVideoInfoModel(),
			YoutubeVideoInfoModel()
	)

	companion object {
		inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
	}

	constructor(builder: Builder) : this(
			builder.context,
			builder.okHttpClient,
			builder.isCacheEnabled,
			builder.isLogEnabled
	)

	private val videoHelper = Helper2(context, client, isCacheEnabled)

	fun loadVideoPreview(
			url: String,
			onSuccess: (VideoPreviewModel) -> Unit,
			onError: ((String, String) -> Unit)? = null
	) {
		if (isLogEnabled) {
			Log.i("VideoService", "loading url: $url")
		}

		val callback: (VideoPreviewModel) -> Unit = { model: VideoPreviewModel ->
			onSuccess.invoke(model)
		}

		val callbackError: (String, String) -> Unit = { requestUrl: String, error: String ->
			onError?.run { invoke(requestUrl, error) }
		}

		try {
			videoInfoModelsList.forEach {
				if (it.checkHostAffiliation(url)) {
					videoHelper.getVideoInfo(url, it, callback, callbackError)
					return
				}
			}
		} catch (e: Exception) {
			callbackError.invoke(url, ERROR_1)
		}
	}

	class Builder {
		var okHttpClient: OkHttpClient = OkHttpClient()
			private set

		var isLogEnabled = false
			private set

		var isCacheEnabled = false
			private set

		var context: Context? = null
			private set

		fun with(context: Context) = apply { this.context = context }

		fun httpClient(client: OkHttpClient) = apply { okHttpClient = client }

		fun enableLog(isEnabled: Boolean) = apply { isLogEnabled = isEnabled }

		fun enableCache(isEnabled: Boolean) = apply { isCacheEnabled = isEnabled }

		fun build() = VideoService(this)
	}
}