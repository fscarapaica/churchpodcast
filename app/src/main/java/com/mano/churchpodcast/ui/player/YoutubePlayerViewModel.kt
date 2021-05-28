package com.mano.churchpodcast.ui.player

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mano.churchpodcast.db.MediaRepository
import com.mano.churchpodcast.model.YoutubeVideo
import com.mano.churchpodcast.ui.adapter.YoutubeVideoAdapter

class YoutubePlayerViewModel(application: Application): AndroidViewModel(application), YoutubeVideoAdapter.Interaction  {

    private val mediaRepository : MediaRepository = MediaRepository(application)

    val currentYoutubeVideo = MutableLiveData<YoutubeVideo>()
    val youtubeVideoList: LiveData<List<YoutubeVideo>>
        get() = Transformations.switchMap(currentYoutubeVideo) {
            mediaRepository.youtubeVideosFrom(it)
        }

    override fun onYoutubeVideoSelected(position: Int, video: YoutubeVideo) {
        currentYoutubeVideo.value = video
    }

}