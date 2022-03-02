package com.gapps.library.utils

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import kotlin.math.roundToInt

fun Context.getWidth(widthRes: Int): Int {
    return if (widthRes <= 0) {
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        size.x
    } else {
        widthRes
    }
}

fun getHeightFromWidth(width: Float?, height: Float?, videoViewWidth: Int): Int {
    return if (width == null || width <= 0 || height == null || height <= 0) {
        videoViewWidth / 16 * 9
    } else {
        val aspectRatio = width / height
        (videoViewWidth / aspectRatio).roundToInt()
    }
}
