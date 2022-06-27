package com.gapps.library.api

import android.content.Context
import android.util.Log
import com.gapps.library.api.models.api.*
import com.gapps.library.api.models.api.base.VideoInfoModel
import com.gapps.library.api.models.api.builder.EmbeddingRequest
import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.api.models.video.base.BaseVideoResponse
import com.gapps.library.utils.errors.EmbeddingError
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.OkHttpClient
import kotlin.coroutines.resumeWithException


class VideoService private constructor(
    context: Context?,
    client: OkHttpClient,
    isCacheEnabled: Boolean,
    private val isLogEnabled: Boolean,
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
        context = builder.context,
        client = builder.okHttpClient,
        isCacheEnabled = builder.isCacheEnabled,
        isLogEnabled = builder.isLogEnabled,
        customModels = builder.customModels
    ) {
        customModels.forEach { custom ->
            videoInfoModelsList.removeAll { it.hostingName == custom.hostingName }
        }

        videoInfoModelsList.addAll(customModels)
    }

    private val videoHelper = createVideoLoadHelper(
        context = context,
        client = client,
        isCacheEnabled = isCacheEnabled
    )

    fun loadVideoPreview(
        url: String,
        onSuccess: (resultModel: VideoPreviewModel) -> Unit,
        onError: ((url: String, message: String) -> Unit)? = null
    ) {
        val request = EmbeddingRequest.Builder()
            .setUrl(url)
            .build()

        loadVideoPreview(request, onSuccess, onError)
    }

    fun loadVideoPreview(
        request: EmbeddingRequest,
        onSuccess: (resultModel: VideoPreviewModel) -> Unit,
        onError: ((url: String, message: String) -> Unit)? = null
    ) {
        if (isLogEnabled) {
            Log.i(TAG, "Loading url: ${request.originalUrl}")
        }

        val callback: (resultModel: VideoPreviewModel) -> Unit = { model: VideoPreviewModel ->
            onSuccess.invoke(model)
        }

        val callbackError: (url: String, message: String) -> Unit =
            { url: String, errorMessage: String ->
                onError?.run { invoke(url, errorMessage) }
            }

        if (request.videoInfoModel == null) {
            request.videoInfoModel =
                videoInfoModelsList.firstOrNull { it.checkHostAffiliation(request.originalUrl) }
        }

        videoHelper.getVideoInfo(
            requestModel = request,
            onSuccess = callback,
            onError = callbackError
        )
    }

    suspend fun loadVideoAsync(url: String): VideoPreviewModel {
        val request = EmbeddingRequest.Builder()
            .setUrl(url)
            .build()

        return loadVideoAsync(request)
    }

    suspend fun loadVideoAsync(request: EmbeddingRequest) =
        suspendCancellableCoroutine<VideoPreviewModel> { continuation ->
            loadVideoPreview(request, { videoPreviewModel ->
                continuation.resumeWith(Result.success(videoPreviewModel))
            }, { url: String, errorMessage: String ->
                continuation.resumeWithException(EmbeddingError(url, errorMessage))
            })
        }

    private fun createVideoLoadHelper(
        context: Context?,
        client: OkHttpClient,
        isCacheEnabled: Boolean
    ) = VideoLoadHelper(
        context = context,
        client = client,
        isCacheEnabled = isCacheEnabled,
        isLogEnabled = isLogEnabled
    )

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

        fun <T : VideoInfoModel<out BaseVideoResponse>> withCustomVideoInfoModels(vararg models: T) =
            apply { this.customModels.addAll(models) }

        fun build() = VideoService(this)
    }
}