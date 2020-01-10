package com.gapps.library.api

import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.api.models.video.dailymotion.DailymotionResponse
import com.gapps.library.api.models.video.facebook.FacebookResponse
import com.gapps.library.api.models.video.rutube.ResponseRutube
import com.gapps.library.api.models.video.vimeo.ResponseVimeo
import com.gapps.library.api.models.video.youtube.ResponseYoutube
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.coroutines.CoroutineContext


class VideoService(
		client: OkHttpClient
) {

	constructor(builder: Builder) : this(
			builder.okHttpClient
	)

	private val videoHelper = Helper(client)

	fun loadVideoPreview(url: String, callback: (VideoPreviewModel) -> Unit) {
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
				else -> {
					callback.invoke(VideoPreviewModel.error())
				}
			}
		} catch (e: Exception) {
			callback.invoke(VideoPreviewModel.error())
		}
	}

	inner class Helper(private val client: OkHttpClient) : CoroutineScope {
		override val coroutineContext: CoroutineContext
			get() = Dispatchers.Main

		fun getFacebookInfo(url: String, callback: (VideoPreviewModel) -> Unit) {
			try {
				launch(coroutineContext) {
					val result = withContext(Dispatchers.IO) {
						val response = client.newCall(Request.Builder().url(url.getFacebookInfoUrl()).build()).execute()
						return@withContext if (response.isSuccessful) {
							val stringBody = response.body()?.string()

							val responseModel = Gson().fromJson<FacebookResponse>(stringBody, FacebookResponse::class.java)
							responseModel.toPreview()
						} else {
							VideoPreviewModel.error()
						}
					}

					callback.invoke(result)
				}
			} catch (e: Exception) {
				e.printStackTrace()
				callback.invoke(VideoPreviewModel.error())
			}
		}

		fun getDailymotionInfo(url: String, callback: (VideoPreviewModel) -> Unit) {
			try {
				launch(coroutineContext) {
					val result = withContext(Dispatchers.IO) {
						val response = client.newCall(Request.Builder().url(url.getDailymotionInfoUrl()).build()).execute()
						return@withContext if (response.isSuccessful) {
							val stringBody = response.body()?.string()

							val responseModel = Gson().fromJson<DailymotionResponse>(stringBody, DailymotionResponse::class.java)
							responseModel.toPreview(url)
						} else {
							VideoPreviewModel.error()
						}
					}

					callback.invoke(result)
				}
			} catch (e: Exception) {
				e.printStackTrace()
				callback.invoke(VideoPreviewModel.error())
			}
		}

		fun getRutubeInfo(url: String, callback: (VideoPreviewModel) -> Unit) {
			try {
				launch(coroutineContext) {
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
			} catch (e: Exception) {
				callback.invoke(VideoPreviewModel.error())
			}
		}

		fun getVimeoInfo(url: String, callback: (VideoPreviewModel) -> Unit) {
			try {
				launch(coroutineContext) {
					val result = withContext(Dispatchers.IO) {
						val response = client.newCall(Request.Builder().url(url.getVimeoInfoUrl()).build()).execute()
						return@withContext if (response.isSuccessful) {
							val stringBody = response.body()?.string()

							try {
								val responseModel = Gson().fromJson<List<ResponseVimeo>>(stringBody, object : TypeToken<List<ResponseVimeo>>() {}.type)

								responseModel.firstOrNull()?.toPreview(url)
										?: VideoPreviewModel.error()
							} catch (e: Exception) {
								VideoPreviewModel.error()
							}
						} else {
							VideoPreviewModel.error()
						}
					}

					callback.invoke(result)
				}
			} catch (e: Exception) {
				e.printStackTrace()
				callback.invoke(VideoPreviewModel.error())
			}
		}

		fun getYoutubeInfo(url: String, callback: (VideoPreviewModel) -> Unit) {
			try {
				launch(coroutineContext) {
					val result = withContext(Dispatchers.IO) {
						val response = client.newCall(Request.Builder().url(url.getYoutubeInfoUrl()).build()).execute()
						return@withContext if (response.isSuccessful) {
							val stringBody = response.body()?.string()

							try {
								val responseModel = Gson().fromJson(stringBody, ResponseYoutube::class.java)

								responseModel.toPreview(url)
							} catch (e: Exception) {
								VideoPreviewModel.error()
							}
						} else {
							VideoPreviewModel.error()
						}
					}

					callback.invoke(result)
				}
			} catch (e: Exception) {
				e.printStackTrace()
				callback.invoke(VideoPreviewModel.error())
			}
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