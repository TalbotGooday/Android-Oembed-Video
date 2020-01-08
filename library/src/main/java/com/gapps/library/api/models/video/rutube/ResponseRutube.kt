package com.gapps.library.api.models.video.rutube


import com.gapps.library.api.models.video.VideoPreviewModel
import com.google.gson.annotations.SerializedName

data class ResponseRutube(
		@SerializedName("action_reason")
		val actionReason: Int = 0,
		@SerializedName("all_tags")
		val allTags: List<AllTag> = listOf(),
		@SerializedName("author")
		val author: Author = Author(),
		@SerializedName("category")
		val category: Category = Category(),
		@SerializedName("club_params")
		val clubParams: String = "",
		@SerializedName("comment_editors")
		val commentEditors: String = "",
		@SerializedName("comments_count")
		val commentsCount: Int = 0,
		@SerializedName("created_ts")
		val createdTs: String = "",
		@SerializedName("description")
		val description: String = "",
		@SerializedName("duration")
		val duration: Int = 0,
		@SerializedName("embed_url")
		val embedUrl: String = "",
		@SerializedName("episode")
		val episode: Any? = Any(),
		@SerializedName("ext_id")
		val extId: Any? = Any(),
		@SerializedName("feed_name")
		val feedName: String = "",
		@SerializedName("feed_subscribers_count")
		val feedSubscribersCount: Int = 0,
		@SerializedName("feed_subscription_url")
		val feedSubscriptionUrl: String = "",
		@SerializedName("feed_url")
		val feedUrl: String = "",
		@SerializedName("for_linked")
		val forLinked: Boolean = false,
		@SerializedName("for_registered")
		val forRegistered: Boolean = false,
		@SerializedName("genres")
		val genres: String = "",
		@SerializedName("has_high_quality")
		val hasHighQuality: Boolean = false,
		@SerializedName("hashtags")
		val hashtags: List<Any> = listOf(),
		@SerializedName("hits")
		val hits: Int = 0,
		@SerializedName("html")
		val html: String = "",
		@SerializedName("id")
		val id: String = "",
		@SerializedName("is_adult")
		val isAdult: Boolean = false,
		@SerializedName("is_classic")
		val isClassic: Boolean = false,
		@SerializedName("is_club")
		val isClub: Boolean = false,
		@SerializedName("is_deleted")
		val isDeleted: Boolean = false,
		@SerializedName("is_external")
		val isExternal: Boolean = false,
		@SerializedName("is_hidden")
		val isHidden: Boolean = false,
		@SerializedName("is_livestream")
		val isLivestream: Boolean = false,
		@SerializedName("is_official")
		val isOfficial: Boolean = false,
		@SerializedName("is_serial")
		val isSerial: Boolean = false,
		@SerializedName("last_update_ts")
		val lastUpdateTs: String = "",
		@SerializedName("music")
		val music: Any? = Any(),
		@SerializedName("pepper")
		val pepper: Any? = Any(),
		@SerializedName("persons")
		val persons: String = "",
		@SerializedName("pg_rating")
		val pgRating: PgRating = PgRating(),
		@SerializedName("picture_url")
		val pictureUrl: String = "",
		@SerializedName("publication_ts")
		val publicationTs: String = "",
		@SerializedName("restrictions")
		val restrictions: Restrictions = Restrictions(),
		@SerializedName("rutube_poster")
		val rutubePoster: Any? = Any(),
		@SerializedName("season")
		val season: Any? = Any(),
		@SerializedName("short_description")
		val shortDescription: String = "",
		@SerializedName("show")
		val show: Any? = Any(),
		@SerializedName("source_url")
		val sourceUrl: String = "",
		@SerializedName("thumbnail_url")
		val thumbnailUrl: String = "",
		@SerializedName("title")
		val title: String = "",
		@SerializedName("track_id")
		val trackId: Int = 0,
		@SerializedName("tv_show_id")
		val tvShowId: Any? = Any(),
		@SerializedName("video_url")
		val videoUrl: String = ""

) {

	private fun width() = try {
		"(?:width=\"(\\d+)\")".toRegex().find(html)?.groups?.get(1)?.value?.toIntOrNull() ?: 0
	} catch (e: Exception) {
		0
	}

	private fun height() = try {
		"(?:height=\"(\\d+)\")".toRegex().find(html)?.groups?.get(1)?.value?.toIntOrNull() ?: 0
	} catch (e: Exception) {
		0
	}

	fun toPreview(): VideoPreviewModel {
		return VideoPreviewModel().apply {
			this.videoTitle = this@ResponseRutube.title
			this.thumbnailUrl = this@ResponseRutube.thumbnailUrl
			this.url = this@ResponseRutube.sourceUrl
			this.videoHosting = VideoPreviewModel.RUTUBE
			this.videoId = this@ResponseRutube.trackId.toString()
			this.linkToPlay = "http://rutube.ru/play/embed/${this.videoId}"
			this.width = this@ResponseRutube.width()
			this.height = this@ResponseRutube.height()
		}
	}
}