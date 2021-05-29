package com.mano.churchpodcast.ext

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentSnapshot
import com.mano.churchpodcast.model.MediaItem
import com.mano.churchpodcast.model.YoutubeVideo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun DocumentSnapshot.toYoutubeVideoList(): List<YoutubeVideo> =
    withContext(Dispatchers.Default) {
        toObject(YoutubeVideoDocument::class.java)!!.videos
    }

suspend fun DocumentSnapshot.toMediaItemList(): List<MediaItem> =
    withContext(Dispatchers.Default) {
        toObject(PlaylistDocument::class.java)!!.mediaItem
    }

@Keep
data class YoutubeVideoDocument(val id: Long = 0, val videos: ArrayList<YoutubeVideo> = ArrayList())

@Keep
data class PlaylistDocument(val mediaItem: ArrayList<MediaItem> = ArrayList())