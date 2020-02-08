package com.mano.hillsongpodcast.ui.home

import android.app.Application
import androidx.lifecycle.*
import com.mano.hillsongpodcast.db.MediaItemRepository
import com.mano.hillsongpodcast.db.MediaItemRoomDB
import com.mano.hillsongpodcast.model.Location
import com.mano.hillsongpodcast.model.MediaItem
import com.mano.hillsongpodcast.ui.adapter.LocationAdapter
import com.mano.hillsongpodcast.ui.adapter.MediaItemAdapter
import kotlinx.coroutines.*

class HomeViewModel(application: Application) : AndroidViewModel(application), LocationAdapter.Interaction, MediaItemAdapter.Interaction {

    private val _locationList = MutableLiveData<List<Location>>().apply {
        value = listOf(Location(0, "Monterrey", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/64/DelValleCity.jpg/450px-DelValleCity.jpg"),
            Location(1, "Buenos Aires", "https://images.clarin.com/2018/11/22/Az0o4AN5D_1256x620__1.jpg", "https://hillsong.com/es/buenosaires/all/podcasts/"),
            Location(2, "Sao Pablo", "https://images.fridaymagazine.ae/1_2304285/imagesList_0/2456872028_main.jpg"))
    }
    val locationList: LiveData<List<Location>> = _locationList

    val mediaItemList: MediatorLiveData<List<MediaItem>> = MediatorLiveData()
    private val mediaItemRepository : MediaItemRepository = MediaItemRepository(MediaItemRoomDB.getDatabase(application).mediaItemDao())

    private val _mediaItemSelected: MutableLiveData<MediaItem> = MutableLiveData()
    val mediaItemSelected : LiveData<MediaItem>
        get() = _mediaItemSelected

    init {
        mediaItemRepository
        mediaItemList.addSource(mediaItemRepository.mediaItemList) {
            mediaItemList.value = it
        }
    }

    fun updateContent() {
        viewModelScope.launch {
            locationList.value?.get(0)?.let {
                mediaItemRepository.fetchMediaItems(it)
            }
        }
    }

    override fun onLocationItemSelected(position: Int, locationItem: Location) {

    }

    override fun onMediaItemSelected(position: Int, mediaItem: MediaItem) {
        _mediaItemSelected.value = mediaItem
    }

}