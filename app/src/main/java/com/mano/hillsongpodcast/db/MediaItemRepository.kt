package com.mano.hillsongpodcast.db

import androidx.lifecycle.LiveData
import com.mano.hillsongpodcast.connector.WebScraper
import com.mano.hillsongpodcast.db.dao.MediaItemDao
import com.mano.hillsongpodcast.model.Location
import com.mano.hillsongpodcast.model.MediaItem

class MediaItemRepository(private val mediaItemDao: MediaItemDao) {

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

}
