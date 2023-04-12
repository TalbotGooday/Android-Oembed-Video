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
import com.gapps.library.api.models.api.*
import com.gapps.library.api.models.api.builder.EmbeddingRequest
import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.videonoapi.R
import com.gapps.videonoapi.databinding.ItemVideoBinding
import com.gapps.videonoapi.utils.extensions.collapse
import com.gapps.videonoapi.utils.extensions.convertDpToPx
import com.gapps.videonoapi.utils.extensions.expand
import com.gapps.videonoapi.utils.extensions.toggleArrow
import com.gapps.videonoapi.utils.recycler_view.MarginItemDecoration


class VideoAdapter(
    private val context: Context,
    private val videoService: VideoService,
    private val listener: Listener
) : RecyclerView.Adapter<VideoAdapter.Holder>(),
    MarginItemDecoration.Listener {

    private var data: MutableList<String> = mutableListOf()
    private val dataExpanded: SparseArray<Boolean> = SparseArray()
    private val loadedData: SparseArray<VideoPreviewModel?> = SparseArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(context)
        return Holder(ItemVideoBinding.inflate(inflater, parent, false))
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

    inner class Holder(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var loadedDataItem: VideoPreviewModel? = null

        fun bind(item: String, listener: Listener) = with(binding) {
            loadedDataItem = loadedData[adapterPosition]

            val isVideoDataVisible = dataExpanded[adapterPosition] ?: false
            val isNeedToLoadData = loadedDataItem == null

            videoLink.text = item

            progress.isVisible = isNeedToLoadData
            iconDropDown.isVisible = isNeedToLoadData.not()
            videoPreviewContainer.isVisible = isVideoDataVisible
            textPreview.isVisible = loadedDataItem?.videoTitle != null

            iconDropDown.toggleArrow(isVideoDataVisible, 0)

            if (loadedDataItem == null) {
                videoService.loadVideoPreview(
                    request = createRequestBuilder(item),
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

            videoDataContainer.setOnClickListener {
                loadedDataItem ?: return@setOnClickListener

                val isExpanded = toggleLayoutExpand(
                    (dataExpanded[bindingAdapterPosition] ?: false).not(),
                    iconDropDown,
                    videoPreviewContainer
                )

                dataExpanded.put(bindingAdapterPosition, isExpanded)
            }
        }

        private fun initVideoView(video: VideoPreviewModel?) = with(binding) {
            video ?: return@with

            val isError = video.errorMessage.isNullOrBlank().not()

            progress.isVisible = false
            iconDropDown.isVisible = true
            textPreview.isVisible = video.videoTitle.isNullOrBlank().not()

            imagePreview.load(video.thumbnailUrl) {
                placeholder(R.drawable.image_shop_1)
                error(R.drawable.image_shop_1)
            }

            textPreview.text = video.videoTitle

            imagePlay.isVisible = isError.not()

            setVideoHostingLogo(videoHostLogo, video.videoHosting)

            if (isError) {
                videoPreviewContainer.setOnClickListener(null)
            } else {
                videoPreviewContainer.setOnClickListener {
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
                YOUTUBE_HOST_NAME -> R.drawable.youtube
                YOUTUBE_MUSIC_HOST_NAME -> R.drawable.youtube_music
                VIMEO_HOST_NAME -> R.drawable.vimeo
                RUTUBE_HOST_NAME -> R.drawable.rutube
                FACEBOOK_HOST_NAME -> R.drawable.ic_fb
                DAILYMOTION_HOST_NAME -> R.drawable.dailymotion
                WISTIA_HOST_NAME -> R.drawable.ic_wistia
                VZAAR_HOST_NAME -> R.drawable.ic_vzaar
                HULU_HOST_NAME -> R.drawable.hulu
                USTREAM_HOST_NAME -> R.drawable.ibm
                TED_TALKS_HOST_NAME -> R.drawable.ted_talks
                COUB_HOST_NAME -> R.drawable.ic_coub
                ULTIMEDIA_HOST_NAME -> R.drawable.ultimedia
                STREAMABLE_HOST_NAME -> R.drawable.streamable
                LOOM_HOST_NAME -> R.drawable.loom
                else -> R.drawable.ic_video
            }

            iconView.setImageResource(icon)
        }
    }

    interface Listener {
        fun onItemClick(item: VideoPreviewModel)
    }
}

fun createRequestBuilder(url: String) = EmbeddingRequest.build {
    setUrl(url)
    headers {
        host(FACEBOOK_HOST_NAME) {
            add("access_token", "")
            addAll(mapOf())
        }
    }
}