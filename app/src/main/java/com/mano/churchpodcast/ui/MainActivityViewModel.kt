package com.mano.churchpodcast.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.mano.churchpodcast.model.Location
import com.mano.churchpodcast.ui.adapter.LocationAdapter
import com.mano.churchpodcast.util.GsonUtil
import com.mano.core.domain.preferences.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val defaultPreferences: Preferences
) : ViewModel(), LocationAdapter.Interaction {

    private val _selectedItem = MutableLiveData<Location>()
    val selectedLocation: LiveData<Location> get() = _selectedItem

    private val _locationList = MutableLiveData<List<Location>>().apply {
        Firebase.remoteConfig["location_list"].let {
            value = GsonUtil.fromJson(it.asString(), Array<Location>::class.java).toList().apply {
                if (isNotEmpty()) {
                    val selectedID = defaultPreferences.getSelectedLocationId()
                    _selectedItem.value = find { location -> location.id == selectedID } ?: get(0)
                }
            }
        }
    }
    val locationList: LiveData<List<Location>>
        get() = _locationList

    override fun onLocationItemSelected(position: Int, item: Location) {
        defaultPreferences.saveSelectedLocationId(item.id)
        _selectedItem.value = item
    }

}