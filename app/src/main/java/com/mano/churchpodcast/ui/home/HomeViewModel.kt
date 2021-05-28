package com.mano.churchpodcast.ui.home

import android.app.Application
import androidx.lifecycle.*
import com.mano.churchpodcast.db.MediaRepository
import com.mano.churchpodcast.model.Location
import com.mano.churchpodcast.model.YoutubeVideo
import com.mano.churchpodcast.ui.adapter.YoutubeVideoAdapter
import kotlinx.coroutines.launch

class HomeViewModel(application: Application): AndroidViewModel(application), YoutubeVideoAdapter.Interaction {

    enum class NetworkStatus { LOADING, DONE }

    private val mediaRepository : MediaRepository = MediaRepository(application)

    private var mutableLocation = MutableLiveData<Location>()
    val youtubeVideoList: LiveData<List<YoutubeVideo>>
        get() = Transformations.switchMap(mutableLocation) {
            mediaRepository.youtubeVideosByChannelId(it)
        }

    private var mutableNetworkStatus: MutableLiveData<NetworkStatus> = MutableLiveData()
    val networkStatus: LiveData<NetworkStatus>
        get() = mutableNetworkStatus

    private val mutableVideo: MutableLiveData<YoutubeVideo> = MutableLiveData()
    val selectedVideo: LiveData<YoutubeVideo>
        get() = mutableVideo

    private var nextPage = 1

    fun onLocationChange(location: Location) {
        viewModelScope.launch {
            getYoutubeVideos(location)
        }
    }

    override fun onYoutubeVideoSelected(position: Int, video: YoutubeVideo) {
        mutableVideo.value = video
    }

    fun getNextPage() {
        viewModelScope.launch {
            mutableLocation.value?.let { location ->
                getYoutubeVideos(location, nextPage)
            }
        }
    }

    fun updateContent() {
        viewModelScope.launch {
            mutableLocation.value?.let { location ->
                getYoutubeVideos(location)
            }
        }
    }

    private suspend fun getYoutubeVideos(location: Location, page: Int = 1) {
        mutableNetworkStatus.value = NetworkStatus.LOADING
        if (mediaRepository.fetchYoutubeVideos(location, page)) {
            nextPage = page + 1
        }
        mutableLocation.value = location
        mutableNetworkStatus.value = NetworkStatus.DONE
    }

}
