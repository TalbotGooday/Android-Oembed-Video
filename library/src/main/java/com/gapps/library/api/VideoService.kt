package com.gapps.library.api

import android.content.Context
import android.util.Log
import com.gapps.library.api.models.api.*
import com.gapps.library.api.models.api.base.VideoInfoModel
import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.api.models.video.base.BaseVideoResponse
import com.gapps.library.utils.errors.ERROR_1
import okhttp3.OkHttpClient


class VideoService(
		context: Context?,
		client: OkHttpClient,
		isCacheEnabled: Boolean,
		val isLogEnabled: Boolean,
		private val customModels: List<VideoInfoModel<out BaseVideoResponse>>
) {
	companion object {
		const val TAG = "VideoService"

		val videoInfoModelsList = mutableListOf(
				LoomVideoInfoModel(),
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
				YoutubeVideoInfoModel(),
				UltimediaVideoInfoModel(),
				StreamableVideoInfoModel()
		)

		inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
	}

	constructor(builder: Builder) : this(
			builder.context,
			builder.okHttpClient,
			builder.isCacheEnabled,
			builder.isLogEnabled,
			builder.customModels
	) {

		customModels.forEach { custom ->
			videoInfoModelsList.removeAll { it.hostingName == custom.hostingName }
		}

		videoInfoModelsList.addAll(customModels)
	}

	private val videoHelper = VideoLoadHelper(context, client, isCacheEnabled, isLogEnabled)

	fun loadVideoPreview(
			url: String,
			onSuccess: (VideoPreviewModel) -> Unit,
			onError: ((String, String) -> Unit)? = null
	) {
		if (isLogEnabled) {
			Log.i(TAG, "loading url: $url")
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

		val customModels: MutableList<VideoInfoModel<out BaseVideoResponse>> = mutableListOf()

		fun with(context: Context) = apply { this.context = context }

		fun httpClient(client: OkHttpClient) = apply { okHttpClient = client }

		fun enableLog(isEnabled: Boolean) = apply { isLogEnabled = isEnabled }

		fun enableCache(isEnabled: Boolean) = apply { isCacheEnabled = isEnabled }

		fun <T : VideoInfoModel<out BaseVideoResponse>> withCustomVideoInfoModels(vararg models: T) = apply { this.customModels.addAll(models) }

		fun build() = VideoService(this)
	}
}