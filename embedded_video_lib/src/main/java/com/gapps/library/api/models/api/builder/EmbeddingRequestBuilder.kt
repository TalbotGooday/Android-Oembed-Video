package com.gapps.library.api.models.api.builder

import com.gapps.library.api.models.api.base.VideoInfoModel

class EmbeddingRequest private constructor(
    var originalUrl: String = "",
    var videoInfoModel: VideoInfoModel<*>? = null,
    private var headers: Headers = emptyMap()
) {
    companion object {
        inline fun build(block: Builder.() -> Unit) =
            Builder().apply(block).build()
    }

    val requestHeaders: Map<String, String>
        get() {
            val headersMap = mutableMapOf<String, String>()

            val hostName = videoInfoModel?.hostingName

            hostName?.let { headers[it] }?.run {
                headersMap.putAll(this)
            }

            headers[Builder.HostBuilder.DEFAULT]?.run {
                headersMap.putAll(this)
            }

            return headersMap
        }

    @EmbeddingRequestBuilderDsl
    class Builder {
        private val request: EmbeddingRequest = EmbeddingRequest()
        private val hostBuilder = HostBuilder()

        fun setUrl(url: String) = apply { request.originalUrl = url }

        fun setVideoInfoModel(videoInfoModel: VideoInfoModel<*>) =
            apply { request.videoInfoModel = videoInfoModel }

        fun headers(headersBuilder: HostBuilder.() -> Unit) = apply {
            this.hostBuilder.apply(headersBuilder)
        }

        fun build(): EmbeddingRequest {
            return request.apply {
                headers = hostBuilder.headers
            }
        }

        @HostBuilderDsl
        @EmbeddingRequestBuilderDsl
        class HostBuilder {
            companion object {
                const val DEFAULT = "default"
            }

            private val builders = mutableMapOf<String, HeadersBuilder>()

            val headers: Headers
                get() = builders.map { it.key to it.value.headers }.toMap()

            fun host(hostName: String, builder: HeadersBuilder.() -> Unit) = apply {
                val builderForHost = getHeadersBuilder(hostName)

                builderForHost.apply(builder)
            }

            fun add(key: String, value: String) = apply {
                getHeadersBuilder(DEFAULT).add(key, value)
            }

            fun addAll(headers: Map<String, String>) = apply {
                getHeadersBuilder(DEFAULT).addAll(headers)
            }

            private fun getHeadersBuilder(hostName: String) =
                builders[hostName] ?: HeadersBuilder()
        }

        @HostBuilderDsl
        class HeadersBuilder {
            val headers = mutableMapOf<String, String>()

            fun add(key: String, value: String) = apply {
                this.headers[key] = value
            }

            fun addAll(headers: Map<String, String>) = apply {
                this.headers.putAll(headers)
            }
        }
    }
}

typealias Headers = Map<String, Map<String, String>>

@DslMarker
annotation class HostBuilderDsl

@DslMarker
annotation class EmbeddingRequestBuilderDsl
