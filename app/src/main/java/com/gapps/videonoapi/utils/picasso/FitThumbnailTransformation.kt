package com.gapps.videonoapi.utils.picasso

import android.graphics.Bitmap
import com.squareup.picasso.Transformation

class FitThumbnailTransformation(private val requiredWidth: Int) : Transformation {
    override fun transform(source: Bitmap): Bitmap {
        val requiredHeight = com.gapps.library.utils.getHeightFromWidth(
            source.width.toFloat(),
            source.height.toFloat(),
            requiredWidth
        )
        val result = Bitmap.createScaledBitmap(source, requiredWidth, requiredHeight, true)
        if (result != source) {
            source.recycle()
        }

        return result
    }

    override fun key(): String {
        return "fitThumbnail()"
    }
}