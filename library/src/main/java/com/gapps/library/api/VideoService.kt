package com.gapps.library.api

import android.util.Log
import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.api.models.video.base.BaseVideoResponse
import com.gapps.library.api.models.video.dailymotion.DailymotionResponse
import com.gapps.library.api.models.video.facebook.FacebookResponse
import com.gapps.library.api.models.video.rutube.ResponseRutube
import com.gapps.library.api.models.video.vimeo.ResponseVimeo
import com.gapps.library.api.models.video.vzaar.VzaarResponse
import com.gapps.library.api.models.video.wistia.WistiaResponse
import com.gapps.library.api.models.video.youtube.ResponseYoutube
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.reflect.Type
import kotlin.coroutines.CoroutineContext


class VideoService(
		client: OkHttpClient,
		val isLogEnabled: Boolean
) {

	constructor(builder: Builder) : this(
			builder.okHttpClient,
			builder.isLogEnabled
	)

	private val videoHelper = Helper(client)

	fun loadVideoPreview(url: String, callback: (VideoPreviewModel) -> Unit) {
		if (isLogEnabled) {
			Log.i("VideoService", "loading url: $url")
		}

		try {
			when {
				url.matches(YOUTUBE_PATTERN.toRegex()) -> {
					videoHelper.getYoutubeInfo(url, callback)
				}
				url.matches(VIMEO_PATTERN.toRegex()) -> {
					videoHelper.getVimeoInfo(url, callback)
				}
				url.matches(RUTUBE_PATTERN.toRegex()) -> {
					videoHelper.getRutubeInfo(url, callback)
				}
				url.matches(FACEBOOK_PATTERN.toRegex()) -> {
					videoHelper.getFacebookInfo(url, callback)
				}
				url.matches(DAILYMOTION_PATTERN.toRegex()) -> {
					videoHelper.getDailymotionInfo(url, callback)
				}
				url.matches(WISTIA_PATTERN.toRegex()) -> {
					videoHelper.getWistiaInfo(url, callback)
				}
				url.matches(VZAAR_PATTERN.toRegex()) -> {
					videoHelper.getVzaarInfoUrl(url, callback)
				}
				else -> {
					callback.invoke(VideoPreviewModel.error(url, ERROR_1))
				}
			}
		} catch (e: Exception) {
			callback.invoke(VideoPreviewModel.error(url, ERROR_1))
		}
	}

	inner class Helper(private val client: OkHttpClient) : CoroutineScope {
		private val job = Job()
		override val coroutineContext: CoroutineContext
			get() = job + Dispatchers.Main

		private var gson = GsonBuilder()
				.setLenient()
				.create()

		fun getFacebookInfo(url: String, callback: (VideoPreviewModel) -> Unit) {
			getVideoInfo(url, url.getFacebookInfoUrl(), FacebookResponse::class.java, callback)
		}

		fun getDailymotionInfo(url: String, callback: (VideoPreviewModel) -> Unit) {
			getVideoInfo(url, url.getDailymotionInfoUrl(), DailymotionResponse::class.java, callback)
		}

		fun getWistiaInfo(url: String, callback: (VideoPreviewModel) -> Unit) {
			getVideoInfo(url, url.getWistiaInfoUrl(), WistiaResponse::class.java, callback)
		}

		fun getVzaarInfoUrl(url: String, callback: (VideoPreviewModel) -> Unit) {
			getVideoInfo(url, url.getVzaarInfoUrl(), VzaarResponse::class.java, callback)
		}

		fun getRutubeInfo(url: String, callback: (VideoPreviewModel) -> Unit) {
			getVideoInfo(url, url.getRutubeInfoUrl(), ResponseRutube::class.java, callback)
		}

		fun getVimeoInfo(url: String, callback: (VideoPreviewModel) -> Unit) {
			getVideoInfo(url, url.getVimeoInfoUrl(), ResponseVimeo::class.java, callback)
		}

		fun getYoutubeInfo(url: String, callback: (VideoPreviewModel) -> Unit) {
			getVideoInfo(url, url.getYoutubeInfoUrl(), ResponseYoutube::class.java, callback)
		}

		private fun getVideoInfo(originalUrl: String?, finalUrl: String?, type: Type?, callback: (VideoPreviewModel) -> Unit) {
			if (finalUrl == null) {
				callback.invoke(VideoPreviewModel.error(originalUrl, ERROR_3))

				return
			}

			runSafeWithBlock(
					{
						val jsonBody = makeCallGetBody(client, finalUrl)

						if (jsonBody == null) {
							withContext(Dispatchers.Main) {
								callback.invoke(VideoPreviewModel.error(originalUrl, "$ERROR_2 \n---> Response is null"))
							}

							return@runSafeWithBlock
						}

						val result = try {
							fromJson(jsonBody, type).toPreview(originalUrl)
						} catch (e: Exception) {
							VideoPreviewModel.error(originalUrl, "$ERROR_2 \n---> ${e.localizedMessage}")
						}

						withContext(Dispatchers.Main) {
							callback.invoke(result)
						}
					},
					{
						callback.invoke(VideoPreviewModel.error(originalUrl, "$ERROR_2 \n---> $it"))
					}
			)
		}

		private fun runSafeWithBlock(action: suspend CoroutineScope.() -> Unit, onError: ((String?) -> Unit)? = null) {
			launch(coroutineContext) {
				try {
					action.invoke(this)
				} catch (error: Throwable) {
					error.printStackTrace()
					onError?.invoke(error.localizedMessage)
				}
			}
		}

		private suspend fun makeCallGetBody(client: OkHttpClient, url: String): JsonElement? {
			return withContext(Dispatchers.IO) {
				val response = client.newCall(Request.Builder().url(url).build()).execute()
				val stringBody = response.body()?.string() ?: return@withContext null
				val jsonObject = JsonParser.parseString(stringBody)

				return@withContext if (jsonObject.isJsonArray) {
					jsonObject.asJsonArray[0]
				} else {
					jsonObject
				}
			}
		}

		private fun fromJson(json: JsonElement?, type: Type?): BaseVideoResponse {
			return gson.fromJson(json, type)
		}
	}

	class Builder {
		lateinit var okHttpClient: OkHttpClient
		var isLogEnabled = false

		fun httpClient(client: OkHttpClient) = apply { okHttpClient = client }

		fun enableLog(isEnabled: Boolean) = apply { isLogEnabled = isEnabled }

		fun build(): VideoService {
			if (::okHttpClient.isInitialized.not()) {
				throw RuntimeException("OkHttpClient is not attached into Builder")
			}

			return VideoService(this)
		}
	}

}