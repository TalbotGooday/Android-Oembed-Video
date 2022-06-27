package com.gapps.library.api

import android.content.Context
import android.util.Log
import com.gapps.library.api.models.api.builder.EmbeddingRequest
import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.api.models.video.base.BaseVideoResponse
import com.gapps.library.cache.getCachedVideoModel
import com.gapps.library.cache.insertModel
import com.gapps.library.utils.errors.ERROR_1
import com.gapps.library.utils.errors.ERROR_2
import com.gapps.library.utils.errors.ERROR_3
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser.parseString
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.reflect.Type
import kotlin.coroutines.CoroutineContext

internal class VideoLoadHelper(
    private val context: Context?,
    private val client: OkHttpClient,
    private val isCacheEnabled: Boolean,
    private val isLogEnabled: Boolean
) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main

    private val gson = GsonBuilder()
        .setLenient()
        .setPrettyPrinting()
        .create()

    fun getVideoInfo(
        requestModel: EmbeddingRequest,
        onSuccess: (resultModel: VideoPreviewModel) -> Unit,
        onError: (url: String, message: String) -> Unit,
    ) {
        val videoInfoModel = requestModel.videoInfoModel
        val originalUrl = requestModel.originalUrl

        if (videoInfoModel == null) {
            onError(originalUrl, ERROR_1)

            return
        }

        val finalUrl = videoInfoModel.getInfoUrl(originalUrl)
        val videoId = videoInfoModel.parseVideoId(originalUrl)

        if (finalUrl == null || videoId == null) {
            onError(originalUrl, ERROR_3)

            return
        }

        val playLink = videoInfoModel.getPlayLink(videoId)

        launch {
            try {
                if (isCacheEnabled) {
                    if (context != null) {
                        val model = getCachedVideoModel(context, playLink)

                        if (model != null) {
                            onSuccess(model)
                            return@launch
                        }
                    }
                }

                val jsonBody = withContext(Dispatchers.IO) {
                    makeCallGetBody(client, finalUrl)
                }

                if (isLogEnabled) {
                    Log.i(
                        VideoService.TAG,
                        "a response from $originalUrl:\n${gson.toJson(jsonBody)}"
                    )
                }

                if (jsonBody == null) {
                    onError(originalUrl, "$ERROR_2 \n---> Response is null")

                    return@launch
                }

                val result = fromJson(jsonBody, videoInfoModel.type)
                    .toPreview(
                        url = originalUrl,
                        linkToPlay = playLink,
                        hostingName = videoInfoModel.hostingName,
                        videoId = videoId
                    )

                onSuccess(result)

                try {
                    if (context != null && isCacheEnabled) {
                        insertModel(context, result)
                    }
                } catch (e: Exception) {
                    onError(originalUrl, "$ERROR_2\n---> ${e.localizedMessage}")
                }
            } catch (e: Exception) {
                onError(originalUrl, ERROR_3)
            }
        }
    }

    private fun makeCallGetBody(client: OkHttpClient, url: String): JsonElement? =
        runBlocking {
            val response = client.newCall(Request.Builder().url(url).build()).execute()
            val stringBody = response.body?.string() ?: return@runBlocking null
            val jsonObject = parseString(stringBody)

            return@runBlocking if (jsonObject.isJsonArray) {
                jsonObject.asJsonArray[0]
            } else {
                jsonObject
            }
        }

    private fun fromJson(json: JsonElement?, type: Type?): BaseVideoResponse {
        return gson.fromJson(json, type)
    }
}
