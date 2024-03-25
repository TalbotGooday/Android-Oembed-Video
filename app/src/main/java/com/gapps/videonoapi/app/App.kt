package com.gapps.videonoapi.app

import android.app.Application
import android.os.Build.VERSION.SDK_INT
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        iniCoil()
    }

    private fun iniCoil() {
        val imageLoader = ImageLoader.Builder(this)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()

        Coil.setImageLoader(imageLoader)
    }
}