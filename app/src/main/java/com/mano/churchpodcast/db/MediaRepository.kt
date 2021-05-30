package com.mano.churchpodcast.db

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.ktx.Firebase
import com.mano.churchpodcast.ext.toMediaItemList
import com.mano.churchpodcast.ext.toYoutubeVideoList
import com.mano.churchpodcast.model.Location
import com.mano.churchpodcast.model.MediaItem
import com.mano.churchpodcast.model.YoutubeVideo
import com.mano.churchpodcast.ext.getDataTimeStamp
import com.mano.churchpodcast.ext.getSharedPreferences
import com.mano.churchpodcast.ext.setDataTimeStamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class MediaRepository(context: Context) {

    companion object {
        const val EXPIRATION_TIME: Long = 2 * 60 * 60 * 1000
        const val PODCAST_COLLECTION_KEY = "podcast"
    }

    private val mediaDB: MediaRoomDB = MediaRoomDB.getDatabase(context)
    private val sharedPreferences = context.getSharedPreferences()

    private val mediaItemDao = lazy { mediaDB.mediaItemDao() }
    private val youtubeVideoDao = lazy { mediaDB.youtubeVideoDao() }
    private val firestoreDB = lazy { FirebaseFirestore.getInstance() }

    val mediaItemList: LiveData<List<MediaItem>>
        get() = mediaItemDao.value.getAllMediaItem()
    val youtubeVideoList: LiveData<List<YoutubeVideo>>
        get() = youtubeVideoDao.value.getAllYoutubeVideo()
    fun youtubeVideosByChannelId(location: Location): LiveData<List<YoutubeVideo>> =
        youtubeVideoDao.value.getYoutubeVideoByChannelId(location.channelId)
    fun youtubeVideosFrom(youtubeVideo: YoutubeVideo): LiveData<List<YoutubeVideo>> =
        youtubeVideoDao.value.getYoutubeVideoFromDate(youtubeVideo.channelId, youtubeVideo.publishedAt)

    suspend fun init() {
        deleteAll()
    }

    suspend fun deleteAll() = withContext(Dispatchers.IO) {
        mediaItemDao.value.deleteAll()
        youtubeVideoDao.value.deleteAll()
    }

    suspend fun fetchYoutubeVideos(location: Location, page: Int): Boolean =
        runCatching {
            firebaseUserAuth {
                withContext(Dispatchers.IO) {
                    val documentReference = firestoreDB.value
                        .collection(location.channelId)
                        .document("$page")
                    val source =
                        if (hasYoutubeVideoDataExpired(location.channelId, page)) Source.SERVER
                        else Source.CACHE

                    await(documentReference.get(source)).run {
                        if (!exists() && metadata.isFromCache) {
                            await(documentReference.get(Source.SERVER))
                        } else this
                    }.apply {
                        if (exists() && !metadata.isFromCache) {
                            sharedPreferences.setDataTimeStamp(location.channelId, page)
                        }
                    }.toYoutubeVideoList().apply {
                        youtubeVideoDao.value.insert(this)
                    }
                }
            }
        }.onFailure {
            Log.d("Fetch Error", "Youtube videos fetching error: $it")
        }.isSuccess

    suspend fun fetchPodcastPlayList(playlist: String): List<MediaItem>? =
        runCatching {
            firebaseUserAuth {
                withContext(Dispatchers.IO) {
                    val documentReference = firestoreDB.value
                        .collection(PODCAST_COLLECTION_KEY)
                        .document(playlist)
                    val source = if (hasPodcastPlaylistDataExpired(playlist)) Source.SERVER
                    else Source.CACHE

                    await(documentReference.get(source)).run {
                        if (!exists() && metadata.isFromCache) {
                            await(documentReference.get(Source.SERVER))
                        } else this
                    }.apply {
                        if (exists() && !metadata.isFromCache) {
                            sharedPreferences.setDataTimeStamp(playlist)
                        }
                    }.toMediaItemList()
                }
            }
        }.onFailure {
            Log.d("Fetch Error", "Playlist fetching error: $it")
        }.getOrNull()

    private fun hasYoutubeVideoDataExpired(channelId: String, page: Int): Boolean =
        hasDataExpired(sharedPreferences.getDataTimeStamp(channelId, page))

    private fun hasPodcastPlaylistDataExpired(playlist: String): Boolean =
        hasDataExpired(sharedPreferences.getDataTimeStamp(playlist))

    private fun hasDataExpired(timeStamp: Long): Boolean =
        (Date().time - timeStamp) > EXPIRATION_TIME

    private suspend fun <T> firebaseUserAuth(invoke: suspend (FirebaseUser?) -> T)
    : T = withContext(Dispatchers.IO) {
        Firebase.auth.run {
            if (currentUser == null) {
                invoke(await(signInAnonymously()).user)
            } else invoke(currentUser)
        }
    }

}
