package com.gapps.library.utils.patterns

import java.util.regex.Pattern

class PatternVideoLinksBuilder {
	private var patterns: ArrayList<VideoLinkPatternItem> = ArrayList()

	private var typeForOther = ""
	private var shouldClearLink = false

	fun addPattern(pattern: Pattern, hosting: String): PatternVideoLinksBuilder {
		this.patterns.add(VideoLinkPatternItem(pattern, hosting))

		return this
	}


	fun other(type: String): PatternVideoLinksBuilder {
		this.typeForOther = type

		return this
	}

	fun clearLink(shouldClear: Boolean): PatternVideoLinksBuilder {
		this.shouldClearLink = shouldClear

		return this
	}

	fun build(editable: CharSequence): List<VideoPatternItem> {
		val rangesMap = mutableMapOf<IntRange, String>()

		val indexes = sortedSetOf(0)

		for (item in patterns) {
			val matcher = item.pattern.matcher(editable)
			while (matcher.find()) {
				val start = matcher.start()
				val end = matcher.end(if(shouldClearLink) 1 else 0)

				indexes.add(start)
				indexes.add(end)
				val intRange = start..end
				rangesMap[intRange] = item.hosting
			}
		}

		if (indexes.last() != editable.length - 1 || editable.length == 1) {
			indexes.add(editable.length)
		}

		return indexes.zipWithNext().map {
			val rightRange = it.first until it.second

			val intRange = IntRange(it.first, it.second)

			return@map VideoPatternItem(
					rangesMap[intRange] ?: "",
					editable.substring(rightRange).trim(),
					intRange
			)
		}.filter { it.hosting.isNotBlank() && it.url.isNotBlank() }
	}

	inner class VideoLinkPatternItem(var pattern: Pattern, var hosting: String)

	inner class VideoPatternItem(var hosting: String, val url: String, val range: IntRange)
}