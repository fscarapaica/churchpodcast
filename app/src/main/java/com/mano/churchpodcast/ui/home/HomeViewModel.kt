package com.mano.churchpodcast.ui.home

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.mano.churchpodcast.db.MediaItemRepository
import com.mano.churchpodcast.db.MediaItemRoomDB
import com.mano.churchpodcast.model.Location
import com.mano.churchpodcast.model.MediaItem
import com.mano.churchpodcast.ui.adapter.LocationAdapter
import com.mano.churchpodcast.ui.adapter.MediaItemAdapter
import com.mano.churchpodcast.util.getLocation
import com.mano.churchpodcast.util.getSharedPreferences
import com.mano.churchpodcast.util.setLocation
import kotlinx.coroutines.*

class HomeViewModel(application: Application) : AndroidViewModel(application), LocationAdapter.Interaction, MediaItemAdapter.Interaction {

    enum class NetworkStatus { LOADING, DONE }

    private val sharedPreferences: SharedPreferences

    private val _locationList = MutableLiveData<List<Location>>().apply {
        value = listOf(Location(0, "Monterrey", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/64/DelValleCity.jpg/450px-DelValleCity.jpg", "monterrey"),
            Location(1, "Buenos Aires", "https://images.clarin.com/2018/11/22/Az0o4AN5D_1256x620__1.jpg", "buenosaires"),
            Location(2, "Sao Pablo", "https://images.fridaymagazine.ae/1_2304285/imagesList_0/2456872028_main.jpg", "saopaulo"))
    }
    val locationList: LiveData<List<Location>>
        get() = _locationList

    val mediaItemList: MediatorLiveData<List<MediaItem>> = MediatorLiveData()
    private val mediaItemRepository : MediaItemRepository = MediaItemRepository(application)

    private val _mediaItemSelected: MutableLiveData<MediaItem> = MutableLiveData()
    val mediaItemSelected : LiveData<MediaItem>
        get() = _mediaItemSelected

    private var _networkStatus: MutableLiveData<NetworkStatus> = MutableLiveData()
    val networkStatus: LiveData<NetworkStatus>
        get() = _networkStatus

    init {
        mediaItemList.addSource(mediaItemRepository.mediaItemList) {
            mediaItemList.value = it
        }

        sharedPreferences = application.applicationContext.getSharedPreferences()
    }

    fun updateContent() {
        viewModelScope.launch {
            _networkStatus.value = NetworkStatus.LOADING
            val location: Location? = sharedPreferences.getLocation() ?: locationList.value?.get(1)
            location?.let {
                mediaItemRepository.fetchMediaItems(it)
            }
            _networkStatus.value = NetworkStatus.DONE
        }
    }

    override fun onLocationItemSelected(position: Int, locationItem: Location) {
        if (_networkStatus.value == NetworkStatus.DONE) {
            viewModelScope.launch {
                sharedPreferences.setLocation(locationItem)
                fetchMediaItems(locationItem)
            }
        }
    }

    override fun onMediaItemSelected(position: Int, mediaItem: MediaItem) {
        if (_networkStatus.value == NetworkStatus.DONE) {
            viewModelScope.launch {
                fetchMediaTrack(mediaItem)
            }
        }
    }

    private suspend fun fetchMediaItems(locationItem: Location) {
        _networkStatus.value = NetworkStatus.LOADING
        mediaItemRepository.fetchMediaItems(locationItem)
        _networkStatus.value = NetworkStatus.DONE
    }

    private suspend fun fetchMediaTrack(mediaItem: MediaItem) {
        _networkStatus.value = NetworkStatus.LOADING
        _mediaItemSelected.value = mediaItem.apply {
            mediaLink = mediaItemRepository.fetchMediaTrack(mediaItem.link!!) ?: ""
        }
        _networkStatus.value = NetworkStatus.DONE
    }

}