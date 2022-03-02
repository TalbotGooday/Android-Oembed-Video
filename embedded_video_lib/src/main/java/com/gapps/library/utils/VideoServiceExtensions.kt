package com.gapps.library.utils

import com.gapps.library.api.VideoService.Companion.videoInfoModelsList
import com.gapps.library.utils.patterns.PatternVideoLinksBuilder
import java.util.regex.Pattern

/**
 * Check url is a video link
 *
 * @return true if url is a link and false otherwise
 */
fun String?.isVideoUrl(): Boolean {
    this ?: return false

    return videoInfoModelsList.joinToString("|") { it.pattern }
        .toRegex()
        .matches(this)
}

/**
 * Looking for video links in a string
 *
 * @return list of [PatternVideoLinksBuilder.VideoPatternItem]
 */
fun String?.findVideos(clearLink: Boolean = false): List<PatternVideoLinksBuilder.VideoPatternItem> {
    this ?: return emptyList()

    val patternBuilder = PatternVideoLinksBuilder()

    videoInfoModelsList.forEach {
        patternBuilder
            .clearLink(clearLink)
            .addPattern(Pattern.compile(it.pattern), it.hostingName)
    }

    return patternBuilder.build(this)
}