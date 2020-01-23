package com.gapps.videonoapi.adapters

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.gapps.library.api.VideoService
import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.utils.getWidth
import com.gapps.videonoapi.R
import com.gapps.videonoapi.utils.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_video.view.*


class VideoAdapter(private val videoService: VideoService, private val listener: Listener) : RecyclerView.Adapter<VideoAdapter.Holder>() {

	private var data: MutableList<String> = mutableListOf()
	private val dataExpanded: SparseArray<Boolean> = SparseArray()
	private val loadedData: SparseArray<VideoPreviewModel?> = SparseArray()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
		return Holder(
				LayoutInflater.from(parent.context)
						.inflate(R.layout.item_video, parent, false)
		)
	}

	override fun getItemCount() = data.size

	override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(data[position], listener)

	fun swapData(data: List<String>) {
		this.data.clear()
		this.data.addAll(data)
		this.loadedData.clear()
		notifyDataSetChanged()
	}

	fun addData(data: List<String>) {
		this.data.addAll(data)
		this.notifyDataSetChanged()
	}

	fun collapseAll() {
		this.dataExpanded.clear()
		this.notifyDataSetChanged()
	}

	inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		private var loadedDataItem: VideoPreviewModel? = null

		fun bind(item: String, listener: Listener) = with(itemView) {
			loadedDataItem = loadedData[adapterPosition]

			val isVideoDataVisible = dataExpanded[adapterPosition] ?: false
			val isNeedToLoadData = loadedDataItem == null

			video_link.text = item

			progress.visibleOrGone(isNeedToLoadData)
			icon_drop_down.visibleOrGone(isNeedToLoadData.not())
			video_preview_container.visibleOrGone(isVideoDataVisible)
			text_preview.visibleOrGone(loadedDataItem?.videoTitle != null)

			icon_drop_down.toggleArrow(isVideoDataVisible, 0)

			if (loadedDataItem == null) {
				videoService.loadVideoPreview(item) { video ->
					loadedData.put(adapterPosition, video)
					loadedDataItem = video

					initVideoView(video)
				}
			} else {
				initVideoView(loadedDataItem)
			}

			video_data_container.setOnClickListener {
				loadedDataItem ?: return@setOnClickListener

				val isExpanded = toggleLayoutExpand(
						(dataExpanded[adapterPosition] ?: false).not(),
						icon_drop_down,
						video_preview_container
				)

				dataExpanded.put(adapterPosition, isExpanded)
			}

			video_preview_container.setOnClickListener {
				val model = loadedDataItem ?: return@setOnClickListener

				listener.onItemClick(model)
			}
		}

		private fun initVideoView(video: VideoPreviewModel?) = with(itemView) {
			video ?: return

			progress.gone()
			icon_drop_down.visible()
			text_preview.visibleOrGone(video.videoTitle.isNullOrBlank().not())

			Picasso.get().load(video.thumbnailUrl).transform(FitThumbnailTransformation(context.getWidth(context.resources.getDimensionPixelSize(com.gapps.library.R.dimen.bv_dialog_width)))).into(image_preview)

			text_preview.text = video.videoTitle

			setVideoHostingLogo(video_host_logo, video.videoHosting)
		}

		private fun toggleLayoutExpand(show: Boolean, arrowView: View, layout: View): Boolean {
			arrowView.toggleArrow(show)

			if (show) {
				layout.expand()
			} else {
				layout.collapse()
			}

			return show
		}

		private fun setVideoHostingLogo(iconView: ImageView, type: String?) {
			type ?: return

			val icon = when (type) {
				VideoPreviewModel.YOUTUBE -> R.drawable.youtube
				VideoPreviewModel.YOUTUBE_MUSIC -> R.drawable.youtube_music
				VideoPreviewModel.VIMEO -> R.drawable.vimeo
				VideoPreviewModel.RUTUBE -> R.drawable.rutube
				VideoPreviewModel.FACEBOOK -> R.drawable.ic_fb
				VideoPreviewModel.DAILYMOTION -> R.drawable.dailymotion
				VideoPreviewModel.WISTIA -> R.drawable.ic_wistia
				VideoPreviewModel.VZAAR -> R.drawable.ic_vzaar
				VideoPreviewModel.HULU -> R.drawable.hulu
				VideoPreviewModel.USTREAM -> R.drawable.ibm
				VideoPreviewModel.TED_TALKS -> R.drawable.ted_talks
				else -> R.drawable.ic_video
			}

			iconView.setImageResource(icon)
		}
	}

	interface Listener {
		fun onItemClick(item: VideoPreviewModel)
	}
}