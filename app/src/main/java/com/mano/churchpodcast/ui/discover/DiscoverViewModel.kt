package com.mano.churchpodcast.ui.discover

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.mano.churchpodcast.db.MediaRepository
import com.mano.churchpodcast.model.MediaItem
import com.mano.churchpodcast.model.Playlist
import com.mano.churchpodcast.ui.adapter.PlaylistAdapter
import com.mano.churchpodcast.util.GsonUtil
import kotlinx.coroutines.*

class DiscoverViewModel(application: Application) : AndroidViewModel(application), PlaylistAdapter.Interaction {

    private val mediaRepository : MediaRepository = MediaRepository(application)

    private val _playlistList = MutableLiveData<List<Playlist>>().apply {
        Firebase.remoteConfig["podcast_playlist"].let {
            value = GsonUtil.fromJson(it.asString(), Array<Playlist>::class.java).toList()
        }
    }
    val playlistList: LiveData<List<Playlist>>
        get() = _playlistList

    private val _mediaItemSelected = MutableLiveData<MediaItem>()
    val mediaItemSelected: LiveData<MediaItem>
        get() = _mediaItemSelected

    override fun onPlaylistSelected(item: Playlist, position: Int, isExpanded: Boolean) {
        playlistList.value?.let { playlistList ->
            playlistList[position].run {
                if (mediaItems.isEmpty()) {
                    viewModelScope.launch {
                        mediaItems = mediaRepository.fetchPodcastPlayList(item.id).orEmpty()
                        _playlistList.value = playlistList
                    }
                } else {
                    mediaItems = emptyList()
                    _playlistList.value = playlistList
                }
            }
        }
    }

    override fun onMediaItemSelected(mediaItem: MediaItem) {
        _mediaItemSelected.value = mediaItem
    }

}