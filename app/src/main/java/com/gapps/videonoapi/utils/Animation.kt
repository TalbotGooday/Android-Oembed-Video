package com.gapps.videonoapi.utils

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation


fun View.toggleArrow(show: Boolean, delay: Long = 200): Boolean {
	return if (show) {
		this.animate().setDuration(delay).rotation(180f)
		true
	} else {
		this.animate().setDuration(delay).rotation(0f)
		false
	}
}

fun View.expand(): Animation {
	this.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
	val targtetHeight = this.measuredHeight

	this.layoutParams.height = 0
	this.visibility = View.VISIBLE
	val a = object : Animation() {
		override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
			this@expand.layoutParams.height = if (interpolatedTime == 1f)
				ViewGroup.LayoutParams.WRAP_CONTENT
			else
				(targtetHeight * interpolatedTime).toInt()
			this@expand.requestLayout()
		}

		override fun willChangeBounds(): Boolean {
			return true
		}
	}

	a.duration = (targtetHeight / this.context.resources.displayMetrics.density).toInt().toLong()
	this.startAnimation(a)
	return a
}

fun View.collapse() {
	val initialHeight = this.measuredHeight

	val a = object : Animation() {
		override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
			if (interpolatedTime == 1f) {
				this@collapse.visibility = View.GONE
			} else {
				this@collapse.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
				this@collapse.requestLayout()
			}
		}

		override fun willChangeBounds(): Boolean {
			return true
		}
	}

	var duration = (initialHeight / this.context.resources.displayMetrics.density).toInt()
	if (duration > 300)
		duration = 300

	a.duration = duration.toLong()
	this.startAnimation(a)
}
