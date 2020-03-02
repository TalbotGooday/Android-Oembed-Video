package com.gapps.library.utils

fun String.getGroupValue(input: CharSequence?, groupIndex: Int): String? {
	input ?: return null

	return this.toRegex().find(input)?.groups?.get(groupIndex)?.value
}