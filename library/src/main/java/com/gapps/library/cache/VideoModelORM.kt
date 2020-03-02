package com.gapps.library.cache

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.gapps.library.api.models.video.VideoPreviewModel
import com.gapps.library.utils.toMD5
import java.util.*


private const val TAG = "VideoModelsORM"
private const val TABLE_NAME = "video_model"
private const val COMMA_SEP = ", "

private const val COLUMN_VIDEO_ID_TYPE = "TEXT PRIMARY KEY"
private const val COLUMN_VIDEO_ID = "video_id"

private const val COLUMN_ID_TYPE = "TEXT"
private const val COLUMN_ID = "id"

private const val COLUMN_URL_TYPE = "TEXT"
private const val COLUMN_URL = "url"

private const val COLUMN_TITLE_TYPE = "TEXT"
private const val COLUMN_TITLE = "title"

private const val COLUMN_THUMBNAIL_TYPE = "TEXT"
private const val COLUMN_THUMBNAIL = "thumbnail"

private const val COLUMN_VIDEO_HOSTING_TYPE = "TEXT"
private const val COLUMN_VIDEO_HOSTING = "video_hosting"

private const val COLUMN_PLAY_LINK_TYPE = "TEXT"
private const val COLUMN_PLAY_LINK = "play_link"

private const val COLUMN_WIDTH_TYPE = "INTEGER"
private const val COLUMN_WIDTH = "width"

private const val COLUMN_HEIGHT_TYPE = "INTEGER"
private const val COLUMN_HEIGHT = "height"

const val SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
		COLUMN_VIDEO_ID + " " + COLUMN_VIDEO_ID_TYPE + COMMA_SEP +
		COLUMN_URL + " " + COLUMN_URL_TYPE + COMMA_SEP +
		COLUMN_ID + " " + COLUMN_ID_TYPE + COMMA_SEP +
		COLUMN_TITLE + " " + COLUMN_TITLE_TYPE + COMMA_SEP +
		COLUMN_THUMBNAIL + " " + COLUMN_THUMBNAIL_TYPE + COMMA_SEP +
		COLUMN_VIDEO_HOSTING + " " + COLUMN_VIDEO_HOSTING_TYPE + COMMA_SEP +
		COLUMN_PLAY_LINK + " " + COLUMN_PLAY_LINK_TYPE + COMMA_SEP +
		COLUMN_WIDTH + " " + COLUMN_WIDTH_TYPE + COMMA_SEP +
		COLUMN_HEIGHT + " " + COLUMN_HEIGHT_TYPE +
		")"

const val SQL_DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"

fun insertModel(context: Context?, post: VideoPreviewModel) {
	val databaseWrapper = DatabaseWrapper(context)
	val database = databaseWrapper.writableDatabase
	val values = modelToContentValues(post)
	val modelId = database.replace(TABLE_NAME, "null", values)
	Log.i(TAG, "Inserted new VideoPreviewModel with ID: $modelId")
	database.close()
}

/**
 * Packs a VideoPreviewModel object into a ContentValues map for use with SQL inserts.
 */
private fun modelToContentValues(videoModel: VideoPreviewModel): ContentValues {
	val values = ContentValues()

	val hash = (videoModel.linkToPlay ?: "").toMD5()
	values.put(COLUMN_ID, hash)
	values.put(COLUMN_URL, videoModel.url)
	values.put(COLUMN_TITLE, videoModel.videoTitle)
	values.put(COLUMN_THUMBNAIL, videoModel.thumbnailUrl)
	values.put(COLUMN_VIDEO_HOSTING, videoModel.videoHosting)
	values.put(COLUMN_VIDEO_ID, videoModel.videoId)
	values.put(COLUMN_PLAY_LINK, videoModel.linkToPlay)
	values.put(COLUMN_WIDTH, videoModel.width)
	values.put(COLUMN_HEIGHT, videoModel.height)
	return values
}

fun getCachedVideoModel(context: Context?, linkToPlay: String): VideoPreviewModel? {
	val databaseWrapper = DatabaseWrapper(context)
	val database = databaseWrapper.readableDatabase
	val cursor = database.rawQuery(
			"SELECT * FROM " + TABLE_NAME
					+ " WHERE " + COLUMN_ID + "='" + linkToPlay.toMD5()
					+ "' " + "LIMIT 1",
			null)
	var model: VideoPreviewModel? = null
	if (cursor.count > 0) {
		cursor.moveToFirst()
		while (!cursor.isAfterLast) {
			model = cursorToPost(cursor)
			cursor.moveToNext()
		}
		Log.i(TAG, "VideoModel loaded successfully.")
	}
	database.close()

	return model
}

fun getCachedVideoModels(context: Context?): List<VideoPreviewModel> {
	val databaseWrapper = DatabaseWrapper(context)
	val database = databaseWrapper.readableDatabase
	val cursor = database.rawQuery("SELECT * FROM $TABLE_NAME", null)
	Log.i(TAG, "Loaded " + cursor.count + " VideoModels...")
	val postList: MutableList<VideoPreviewModel> = ArrayList()
	if (cursor.count > 0) {
		cursor.moveToFirst()
		while (!cursor.isAfterLast) {
			val post = cursorToPost(cursor)
			postList.add(post)
			cursor.moveToNext()
		}
		Log.i(TAG, "VideoModels loaded successfully.")
	}
	database.close()
	return postList
}

/**
 * Populates a VideoPreviewModel object with data from a Cursor
 *
 * @param cursor
 * @return
 */
private fun cursorToPost(cursor: Cursor): VideoPreviewModel {
	val post = VideoPreviewModel()

	post.url = cursor.getString(cursor.getColumnIndex(COLUMN_URL))
	post.videoTitle = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
	post.thumbnailUrl = cursor.getString(cursor.getColumnIndex(COLUMN_THUMBNAIL))
	post.videoHosting = cursor.getString(cursor.getColumnIndex(COLUMN_VIDEO_HOSTING))
	post.videoId = cursor.getString(cursor.getColumnIndex(COLUMN_VIDEO_ID))
	post.linkToPlay = cursor.getString(cursor.getColumnIndex(COLUMN_PLAY_LINK))
	post.width = cursor.getInt(cursor.getColumnIndex(COLUMN_WIDTH))
	post.height = cursor.getInt(cursor.getColumnIndex(COLUMN_HEIGHT))
	return post
}