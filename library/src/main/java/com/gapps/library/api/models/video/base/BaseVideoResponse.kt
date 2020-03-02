package com.gapps.library.api.models.video.base

import com.gapps.library.api.models.video.VideoPreviewModel

interface BaseVideoResponse {
	fun toPreview(url: String?, linkToPlay: String, hostingName: String, videoId: String): VideoPreviewModel
}