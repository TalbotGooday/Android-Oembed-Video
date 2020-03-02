package com.gapps.library.api

import android.content.Context
import com.gapps.library.api.models.api.base.VideoInfoModel
import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.api.models.video.base.BaseVideoResponse
import com.gapps.library.cache.getCachedVideoModel
import com.gapps.library.cache.insertModel
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser.parseString
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.reflect.Type
import kotlin.coroutines.CoroutineContext

internal class Helper2(private val context: Context?, private val client: OkHttpClient, val isCacheEnabled: Boolean) : CoroutineScope {
	private val job = Job()
	override val coroutineContext: CoroutineContext
		get() = job + Dispatchers.Main

	private val databaseContext = job + Dispatchers.IO

	private var gson = GsonBuilder()
			.setLenient()
			.create()

	fun getVideoInfo(
			originalUrl: String?,
			videoInfoModel: VideoInfoModel<*>,
			onSuccess: (VideoPreviewModel) -> Unit,
			onError: (String, String) -> Unit
	) {
		val finalUrl = videoInfoModel.getInfoUrl(originalUrl)
		val videoId = videoInfoModel.parseVideoId(originalUrl)

		if (finalUrl == null || videoId == null) {
			onError.invoke(originalUrl ?: "null url", ERROR_3)

			return
		}

		val playLink = videoInfoModel.getPlayLink(videoId)

		launch {
			if (isCacheEnabled) {
				if (context != null) {
					try {
						val model = withContext(databaseContext) {
							getCachedVideoModel(context, playLink)
						}

						if (model != null) {
							onSuccess.invoke(model)
							return@launch
						}
					} catch (e: Exception) {
						e.printStackTrace()
					}
				}
			}
			try {
				val jsonBody = makeCallGetBody(client, finalUrl)

				if (jsonBody == null) {
					onSuccess.invoke(VideoPreviewModel.error(originalUrl, "$ERROR_2 \n---> Response is null"))

					return@launch
				}

				val result = fromJson(jsonBody, videoInfoModel.type)
						.toPreview(originalUrl, playLink, videoInfoModel.hostingName, videoId)

				onSuccess.invoke(result)

				try {
					if (context != null && isCacheEnabled) {
						withContext(databaseContext) {
							insertModel(context, result)
						}
					}
				} catch (e: Exception) {
					e.printStackTrace()
				}
			} catch (e: Exception) {
				onError.invoke(originalUrl ?: "null url", "$ERROR_2 \n---> ${e.localizedMessage}")
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
