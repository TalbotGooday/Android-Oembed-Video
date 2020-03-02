package com.gapps.library.api.models.video.vimeo

import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.api.models.video.base.BaseVideoResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VimeoResponse : BaseVideoResponse {
	@SerializedName("id")
	@Expose
	var id: Int? = null
	@SerializedName("title")
	@Expose
	var title: String? = null
	@SerializedName("description")
	@Expose
	var description: String? = null
	@SerializedName("url")
	@Expose
	var url: String? = null
	@SerializedName("upload_date")
	@Expose
	var uploadDate: String? = null
	@SerializedName("thumbnail_small")
	@Expose
	var thumbnailSmall: String? = null
	@SerializedName("thumbnail_medium")
	@Expose
	var thumbnailMedium: String? = null
	@SerializedName("thumbnail_large")
	@Expose
	var thumbnailLarge: String? = null
	@SerializedName("user_id")
	@Expose
	var userId: Int? = null
	@SerializedName("user_name")
	@Expose
	var userName: String? = null
	@SerializedName("user_url")
	@Expose
	var userUrl: String? = null
	@SerializedName("user_portrait_small")
	@Expose
	var userPortraitSmall: String? = null
	@SerializedName("user_portrait_medium")
	@Expose
	var userPortraitMedium: String? = null
	@SerializedName("user_portrait_large")
	@Expose
	var userPortraitLarge: String? = null
	@SerializedName("user_portrait_huge")
	@Expose
	var userPortraitHuge: String? = null
	@SerializedName("stats_number_of_likes")
	@Expose
	var statsNumberOfLikes: Int? = null
	@SerializedName("stats_number_of_plays")
	@Expose
	var statsNumberOfPlays: Int? = null
	@SerializedName("stats_number_of_comments")
	@Expose
	var statsNumberOfComments: Int? = null
	@SerializedName("duration")
	@Expose
	var duration: Int? = null
	@SerializedName("width")
	@Expose
	var width: Int? = null
	@SerializedName("height")
	@Expose
	var height: Int? = null
	@SerializedName("tags")
	@Expose
	var tags: String? = null
	@SerializedName("embed_privacy")
	@Expose
	var embedPrivacy: String? = null

	override fun toPreview(url: String?, linkToPlay: String, hostingName: String, videoId: String): VideoPreviewModel {
		return VideoPreviewModel(url, linkToPlay, hostingName, videoId).apply {
			this.thumbnailUrl = this@VimeoResponse.thumbnailLarge
			this.videoTitle = this@VimeoResponse.title
			this.width = this@VimeoResponse.width ?: 0
			this.height = this@VimeoResponse.height ?: 0
		}
	}
}