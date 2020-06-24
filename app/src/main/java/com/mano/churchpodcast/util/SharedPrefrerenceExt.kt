package com.mano.churchpodcast.util

import android.content.Context
import android.content.SharedPreferences
import com.mano.churchpodcast.model.Location


private const val SHARED_PREFERENCE_NAME = "churchpodcast_preference"
fun Context.getSharedPreferences(): SharedPreferences = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

private const val KEY_LOCATION = "location"
fun SharedPreferences.setLocation(location: Location) = edit().putString(KEY_LOCATION,GsonUtil.toJson(location)).apply()
fun SharedPreferences.getLocation(): Location? = getString(KEY_LOCATION, null)?.let {
    GsonUtil.fromJson(it, Location::class.java)
}