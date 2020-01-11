package com.gapps.library.api.models.video.base

import com.gapps.library.api.models.video.VideoPreviewModel

interface BaseVideoResponse {
	fun toPreview(url: String? = null): VideoPreviewModel
	fun getVideoId(url: String? = null): String?
}