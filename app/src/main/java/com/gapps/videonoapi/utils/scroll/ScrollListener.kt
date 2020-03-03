package com.gapps.videonoapi.utils.scroll

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ScrollListener(private val scrollValue: Float, private val  action: (Boolean) -> Unit):  RecyclerView.OnScrollListener() {
	private var totalDy = 0f

	override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
		super.onScrolled(recyclerView, dx, dy)
		val layoutManager = recyclerView.layoutManager as LinearLayoutManager

		val firstVisiblePos = layoutManager.findFirstVisibleItemPosition()

		if (firstVisiblePos == 0) {
			action.invoke(false)
		} else {
			if (dy > 0) {
				totalDy += dy.toFloat()
				if (totalDy > scrollValue) {
					totalDy = 0f
					action.invoke(true)

				}
			} else {
				totalDy += dy.toFloat()
				if (totalDy < -scrollValue) {
					action.invoke(false)

					totalDy = 0f
				}
			}
		}
	}

}