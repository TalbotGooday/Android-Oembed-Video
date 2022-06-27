package com.gapps.videonoapi.ui.main.adapters

import android.content.Context
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.gapps.library.api.VideoService
import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.videonoapi.R
import com.gapps.videonoapi.utils.extensions.collapse
import com.gapps.videonoapi.utils.extensions.convertDpToPx
import com.gapps.videonoapi.utils.extensions.expand
import com.gapps.videonoapi.utils.extensions.toggleArrow
import com.gapps.videonoapi.utils.recycler_view.MarginItemDecoration
import kotlinx.android.synthetic.main.item_video.view.*


class VideoAdapter(
    private val context: Context,
    private val videoService: VideoService,
    private val listener: Listener
) : RecyclerView.Adapter<VideoAdapter.Holder>(),
    MarginItemDecoration.Listener {

    private var data: MutableList<String> = mutableListOf()
    private val dataExpanded: SparseArray<Boolean> = SparseArray()
    private val loadedData: SparseArray<VideoPreviewModel?> = SparseArray()
    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(inflater.inflate(R.layout.item_video, parent, false))
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: Holder, position: Int) =
        holder.bind(data[position], listener)

    override fun getTopMargin(position: Int): Int {
        return 0
    }

    override fun getBottomMargin(position: Int): Int {
        return if (position == itemCount - 1) {
            context.convertDpToPx(75f).toInt()
        } else {
            0
        }
    }

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

            progress.isVisible = isNeedToLoadData
            icon_drop_down.isVisible = isNeedToLoadData.not()
            video_preview_container.isVisible = isVideoDataVisible
            text_preview.isVisible = loadedDataItem?.videoTitle != null

            icon_drop_down.toggleArrow(isVideoDataVisible, 0)

            if (loadedDataItem == null) {
                videoService.loadVideoPreview(
                    url = item,
                    onSuccess = { video ->
                        loadedData.put(bindingAdapterPosition, video)
                        loadedDataItem = video

                        initVideoView(video)
                    },
                    onError = { url, errorMessage ->
                        Log.e("MainActivity", "$errorMessage \n $url")

                        val video = VideoPreviewModel.Builder()
                            .setUrl(url)
                            .setVideoTitle("Not found")
                            .setErrorMessage(errorMessage)
                            .setThumbnailUrl("https://c.tenor.com/IHdlTRsmcS4AAAAM/404.gif")
                            .build()

                        loadedData.put(bindingAdapterPosition, video)
                        loadedDataItem = video

                        initVideoView(video)
                    })
            } else {
                initVideoView(loadedDataItem)
            }

            video_data_container.setOnClickListener {
                loadedDataItem ?: return@setOnClickListener

                val isExpanded = toggleLayoutExpand(
                    (dataExpanded[bindingAdapterPosition] ?: false).not(),
                    icon_drop_down,
                    video_preview_container
                )

                dataExpanded.put(bindingAdapterPosition, isExpanded)
            }
        }

        private fun initVideoView(video: VideoPreviewModel?) = with(itemView) {
            video ?: return

            val isError = video.errorMessage.isNullOrBlank().not()

            progress.isVisible = false
            icon_drop_down.isVisible = true
            text_preview.isVisible = video.videoTitle.isNullOrBlank().not()

            image_preview.load(video.thumbnailUrl) {
                placeholder(R.drawable.image_shop_1)
                error(R.drawable.image_shop_1)
            }

            text_preview.text = video.videoTitle

            image_play.isVisible = isError.not()

            setVideoHostingLogo(video_host_logo, video.videoHosting)

            if (isError) {
                video_preview_container.setOnClickListener(null)
            } else {
                video_preview_container.setOnClickListener {
                    val model = loadedDataItem ?: return@setOnClickListener

                    listener.onItemClick(model)
                }
            }
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
                "YouTube" -> R.drawable.youtube
                "YouTube Music" -> R.drawable.youtube_music
                "Vimeo" -> R.drawable.vimeo
                "Rutube" -> R.drawable.rutube
                "Facebook" -> R.drawable.ic_fb
                "Dailymotion" -> R.drawable.dailymotion
                "Wistia" -> R.drawable.ic_wistia
                "Vzaar" -> R.drawable.ic_vzaar
                "Hulu" -> R.drawable.hulu
                "Ustream" -> R.drawable.ibm
                "Ted Talks" -> R.drawable.ted_talks
                "Coub" -> R.drawable.ic_coub
                "Ultimedia" -> R.drawable.ultimedia
                "Streamable" -> R.drawable.streamable
                "Loom" -> R.drawable.loom
                else -> R.drawable.ic_video
            }

            iconView.setImageResource(icon)
        }
    }

    interface Listener {
        fun onItemClick(item: VideoPreviewModel)
    }
}