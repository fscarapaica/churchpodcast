package com.mano.churchpodcast.db

import android.content.Context
import androidx.lifecycle.LiveData
import com.mano.churchpodcast.connector.WebScraper
import com.mano.churchpodcast.db.dao.MediaItemDao
import com.mano.churchpodcast.model.Location
import com.mano.churchpodcast.model.MediaItem

class MediaItemRepository(context: Context) {

    private val mediaItemDao: MediaItemDao = MediaItemRoomDB.getDatabase(context).mediaItemDao()

    val mediaItemList : LiveData<List<MediaItem>> = mediaItemDao.getAllMediaItem()

    suspend fun insertMediaItem(mediaItemList: List<MediaItem>) {
        mediaItemDao.insert(mediaItemList)
    }

    suspend fun fetchMediaItems(location: Location) {
        runCatching {
            WebScraper.fetchMediaItems("https://hillsong.com/es/${location.link}/all/podcasts/")
        }.onSuccess {
            mediaItemDao.deleteAll()
            mediaItemDao.insert(it)
        }.onFailure {

        }
    }

    suspend fun fetchMediaTrack(link: String): String? {
        runCatching {
            return WebScraper.fetchMediaTrack(link)
        }.onFailure {
            return it.message
        }
        return ""
    }

    suspend fun deleteAll() {
        runCatching {
            mediaItemDao.deleteAll()
        }.onFailure {

        }
    }

}
