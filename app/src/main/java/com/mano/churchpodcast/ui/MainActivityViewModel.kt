package com.mano.churchpodcast.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.mano.churchpodcast.model.Location
import com.mano.churchpodcast.ui.adapter.LocationAdapter
import com.mano.churchpodcast.util.GsonUtil
import com.mano.churchpodcast.ext.getSelectedPosition
import com.mano.churchpodcast.ext.getSharedPreferences
import com.mano.churchpodcast.ext.setSelectedPosition

class MainActivityViewModel(application: Application):
    AndroidViewModel(application),
    LocationAdapter.Interaction {

    private val sharedPreferences = application.getSharedPreferences()

    private val mutableSelectedItem = MutableLiveData<Location>().apply {
        application.applicationContext.getSharedPreferences().getSelectedPosition()
    }
    val selectedLocation: LiveData<Location> get() = mutableSelectedItem

    private val _locationList = MutableLiveData<List<Location>>().apply {
        Firebase.remoteConfig["location_list"].let {
            value = GsonUtil.fromJson(it.asString(), Array<Location>::class.java).toList().apply {
                if (isNotEmpty()) {
                    mutableSelectedItem.value = sharedPreferences
                        .getSelectedPosition()
                        .let { selectedPosition ->
                            get(selectedPosition)
                        }
                }
            }
        }
    }
    val locationList: LiveData<List<Location>>
        get() = _locationList

    override fun onLocationItemSelected(position: Int, item: Location) {
        sharedPreferences.setSelectedPosition(position)
        mutableSelectedItem.value = item
    }

}