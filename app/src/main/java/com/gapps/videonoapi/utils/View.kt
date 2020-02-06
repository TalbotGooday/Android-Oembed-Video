package com.gapps.videonoapi.utils

import android.view.View
import android.view.animation.LinearInterpolator

/**
 * Set the visibility state of this view to [View.VISIBLE] or [View.GONE]
 *
 * @param visible
 */
fun View.visibleOrGone(visible: Boolean) {
	visibility = if (visible) View.VISIBLE else View.GONE
}

/**
 * Set the visibility state of this view to [View.GONE]
 */
fun View.gone() {
	if (visibility != View.GONE) {
		visibility = View.GONE
	}
}

/**
 * Set the visibility state of this view to [View.VISIBLE]
 */
fun View.visible() {
	if (visibility != View.VISIBLE) {
		visibility = View.VISIBLE
	}
}

fun View.alphaSmooth(value: Float) {
	this.animate().apply {
		interpolator = LinearInterpolator()
		duration = 200
		alpha(value)
		start()
	}
}