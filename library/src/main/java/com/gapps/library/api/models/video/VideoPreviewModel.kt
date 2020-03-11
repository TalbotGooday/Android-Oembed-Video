package com.gapps.library.api.models.video

class VideoPreviewModel() {
	var url: String? = null
	var videoTitle: String? = null
	var thumbnailUrl: String? = null
	var videoHosting: String? = null
	var videoId: String? = null
	var linkToPlay: String? = null
	var width = 0
	var height = 0
	var errorMessage: String? = null

	constructor(url: String?, linkToPlay: String, hostingName: String, videoId: String) : this() {
		this.url = url
		this.videoHosting = hostingName
		this.videoId = videoId
		this.linkToPlay = linkToPlay
	}

	companion object {
		const val ERROR_404 = "Not found"

		fun error(url: String?, message: String? = null) = VideoPreviewModel().apply {
			this.url = url
			this.videoTitle = ERROR_404
			this.thumbnailUrl = "http://euonthemove.eu/wp-content/uploads/2017/05/no-video.jpg"
			this.errorMessage = message
		}
	}
}