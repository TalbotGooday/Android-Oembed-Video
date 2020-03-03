package com.gapps.videonoapi.utils.recycler_view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(private val listener: Listener, val top: Boolean = true, val bottom: Boolean = false) : RecyclerView.ItemDecoration() {
	override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
		val position = parent.getChildAdapterPosition(view)

		with(outRect) {
			if (this@MarginItemDecoration.top) {
				top = listener.getTopMargin(position)
			}

			if (this@MarginItemDecoration.bottom) {
				bottom = listener.getBottomMargin(position)
			}
		}
	}

	interface Listener {
		fun getTopMargin(position: Int): Int
		fun getBottomMargin(position: Int): Int
	}
}