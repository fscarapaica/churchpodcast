package com.mano.core.data

import android.content.SharedPreferences
import com.mano.core.domain.preferences.Preferences
import com.mano.core.domain.preferences.Preferences.Companion.KEY_SELECTED_LOCATION_ID

class DefaultPreferences(
    private val sharedPreferences: SharedPreferences
): Preferences {

    override fun saveSelectedLocationId(locationId: Int) {
        sharedPreferences
            .edit()
            .putInt(KEY_SELECTED_LOCATION_ID, locationId)
            .apply()
    }

    override fun getSelectedLocationId(): Int = sharedPreferences.getInt(KEY_SELECTED_LOCATION_ID, -1)

}