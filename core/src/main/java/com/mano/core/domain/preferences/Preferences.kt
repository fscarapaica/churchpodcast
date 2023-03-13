package com.mano.core.domain.preferences

import com.mano.core.domain.model.Location

interface Preferences {

    fun saveSelectedLocationId(locationId: Int)

    fun getSelectedLocationId(): Int

    companion object {
        const val KEY_SELECTED_LOCATION_ID = "selected_location_id"
    }

}