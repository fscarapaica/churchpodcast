package com.mano.churchpodcast.ext

import android.content.Context
import android.content.SharedPreferences
import java.util.*

private const val SHARED_PREFERENCE_NAME = "churchpodcast_preference"
fun Context.getSharedPreferences(): SharedPreferences = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

private const val KEY_SELECTED_POSITION = "selected_position"

fun SharedPreferences.setSelectedPosition(selectedPosition: Int) =
    edit().putInt(KEY_SELECTED_POSITION, selectedPosition).apply()

fun SharedPreferences.getSelectedPosition(): Int =
    getInt(KEY_SELECTED_POSITION, 0)

fun SharedPreferences.setDataTimeStamp(channelId: String, page: Int) =
    edit().putLong("$channelId$page", Date().time).apply()

fun SharedPreferences.getDataTimeStamp(channelId: String, page: Int) =
    getLong("$channelId$page", 0)

fun SharedPreferences.setDataTimeStamp(playlist: String) =
    edit().putLong("podcast$playlist", Date().time).apply()

fun SharedPreferences.getDataTimeStamp(playlist: String) =
    getLong("podcast$playlist", 0)