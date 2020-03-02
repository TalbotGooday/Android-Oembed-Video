package com.gapps.library.api

import android.util.Log
import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.api.models.video.base.BaseVideoResponse
import com.gapps.library.api.models.video.coub.CoubResponse
import com.gapps.library.api.models.video.dailymotion.DailymotionResponse
import com.gapps.library.api.models.video.facebook.FacebookResponse
import com.gapps.library.api.models.video.hulu.HuluResponse
import com.gapps.library.api.models.video.rutube.RutubeResponse
import com.gapps.library.api.models.video.ted.TedTalksResponse
import com.gapps.library.api.models.video.ustream.UstreamResponse
import com.gapps.library.api.models.video.vimeo.VimeoResponse
import com.gapps.library.api.models.video.vzaar.VzaarResponse
import com.gapps.library.api.models.video.wistia.WistiaResponse
import com.gapps.library.api.models.video.youtube.YoutubeResponse
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.JsonParser.parseString
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
				url.matches(HULU_PATTERN.toRegex()) -> {
					videoHelper.getHuluInfo(url, callback)
				}
				url.matches(USTREAM_PATTERN.toRegex()) -> {
					videoHelper.getUstreamInfo(url, callback)
				}
				url.matches(TED_TALKS_PATTERN.toRegex()) -> {
					videoHelper.getTedTalksInfo(url, callback)
				}
				url.matches(COUB_PATTERN.toRegex()) -> {
					videoHelper.getCoubInfo(url, callback)
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

		fun getUstreamInfo(url: String, callback: (VideoPreviewModel) -> Unit) {
			getVideoInfo(url, url.getUstreamInfoUrl(), UstreamResponse::class.java, callback)
		}

		fun getTedTalksInfo(url: String, callback: (VideoPreviewModel) -> Unit) {
			getVideoInfo(url, url.getTedTalksInfoUrl(), TedTalksResponse::class.java, callback)
		}


		fun getCoubInfo(url: String, callback: (VideoPreviewModel) -> Unit) {
			getVideoInfo(url, url.getCoubInfoUrl(), CoubResponse::class.java, callback)
		}

		fun getHuluInfo(url: String, callback: (VideoPreviewModel) -> Unit) {
			getVideoInfo(url, url.getHuluInfoUrl(), HuluResponse::class.java, callback)
		}

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
			getVideoInfo(url, url.getRutubeInfoUrl(), RutubeResponse::class.java, callback)
		}

		fun getVimeoInfo(url: String, callback: (VideoPreviewModel) -> Unit) {
			getVideoInfo(url, url.getVimeoInfoUrl(), VimeoResponse::class.java, callback)
		}

		fun getYoutubeInfo(url: String, callback: (VideoPreviewModel) -> Unit) {
			val type = YoutubeResponse::class.java
			getVideoInfo(url, url.getYoutubeInfoUrl(), type, callback)
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
				val jsonObject = parseString(stringBody)

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
		var okHttpClient: OkHttpClient = OkHttpClient()
		private set

		var isLogEnabled = false

		fun httpClient(client: OkHttpClient) = apply { okHttpClient = client }

		fun enableLog(isEnabled: Boolean) = apply { isLogEnabled = isEnabled }

		fun build(): VideoService {
			return VideoService(this)
		}
	}
}