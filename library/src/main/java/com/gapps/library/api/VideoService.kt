package com.gapps.library.api

import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.api.models.video.rutube.ResponseRutube
import com.gapps.library.api.models.video.vimeo.ResponseVimeo
import com.gapps.library.api.models.video.youtube.ResponseYoutube
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.coroutines.CoroutineContext


class VideoService(
		internal val client: OkHttpClient
) : CoroutineScope {

	override val coroutineContext: CoroutineContext
		get() = Dispatchers.Default

	constructor(builder: Builder) : this(
			builder.okHttpClient
	)

	fun loadVideoPreview(url: String, callback: (VideoPreviewModel) -> Unit) {
		when {
			url.matches(YOUTUBE_PATTERN.toRegex()) -> {
				getYoutubeInfo(url, callback)
			}
			url.matches(VIMEO_PATTERN.toRegex()) -> {
				getVimeoInfo(url, callback)
			}
			url.matches(RUTUBE_PATTERN.toRegex()) -> {
				getRutubeInfo(url, callback)
			}
			else -> {
				callback.invoke(VideoPreviewModel.error())
			}
		}
	}

	private fun getYoutubeInfo(url: String, callback: (VideoPreviewModel) -> Unit) {
		GlobalScope.launch(Dispatchers.Main) {
			val result = withContext(Dispatchers.IO) {
				val response = client.newCall(Request.Builder().url(url.getYoutubeInfoUrl()).build()).execute()
				return@withContext if (response.isSuccessful) {
					val stringBody = response.body()?.string()

					val responseModel = Gson().fromJson(stringBody, ResponseYoutube::class.java)

					responseModel.toPreview(url)
				} else {
					VideoPreviewModel.error()
				}
			}

			callback.invoke(result)
		}
	}

	private fun getVimeoInfo(url: String, callback: (VideoPreviewModel) -> Unit) {
		GlobalScope.launch(Dispatchers.Main) {
			val result = withContext(Dispatchers.IO) {
				val response = client.newCall(Request.Builder().url(url.getVimeoInfoUrl()).build()).execute()
				return@withContext if (response.isSuccessful) {
					val stringBody = response.body()?.string()

					val responseModel = Gson().fromJson<List<ResponseVimeo>>(stringBody, object : TypeToken<List<ResponseVimeo>>() {}.type)

					responseModel.firstOrNull()?.toPreview(url) ?: VideoPreviewModel.error()
				} else {
					VideoPreviewModel.error()
				}
			}

			callback.invoke(result)
		}
	}


	private fun getRutubeInfo(url: String, callback: (VideoPreviewModel) -> Unit) {
		GlobalScope.launch(Dispatchers.Main) {
			val result = withContext(Dispatchers.IO) {
				val response = client.newCall(Request.Builder().url(url.getRutubeInfoUrl()).build()).execute()
				return@withContext if (response.isSuccessful) {
					val stringBody = response.body()?.string()

					val responseModel = Gson().fromJson<ResponseRutube>(stringBody, ResponseRutube::class.java)

					responseModel.toPreview()
				} else {
					VideoPreviewModel.error()
				}
			}

			callback.invoke(result)
		}
	}

	class Builder {
		lateinit var okHttpClient: OkHttpClient

		fun httpClient(client: OkHttpClient) = apply { okHttpClient = client }

		fun build(): VideoService {
			if (::okHttpClient.isInitialized.not()) {
				throw RuntimeException("OkHttpClient is not attached into Builder")
			}

			return VideoService(this)
		}
	}

}