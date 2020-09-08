package com.gapps.library.cache

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseWrapper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
	companion object {
		private const val TAG = "DatabaseWrapper"
		private const val DATABASE_NAME = "vna.db"
		private const val DATABASE_VERSION = 1
	}

	/**
	 * Called if the database named DATABASE_NAME doesn't exist in order to create it.
	 */
	override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
		Log.i(TAG, "Creating database [$DATABASE_NAME v.$DATABASE_VERSION]...\n$SQL_CREATE_TABLE")
		sqLiteDatabase.execSQL(SQL_CREATE_TABLE)
	}

	/**
	 * Called when the DATABASE_VERSION is increased.
	 */
	override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
		Log.i(TAG, "Upgrading database [$DATABASE_NAME v.$oldVersion] to [$DATABASE_NAME v.$newVersion]...")
		sqLiteDatabase.execSQL(SQL_DROP_TABLE)
		onCreate(sqLiteDatabase)
	}
}