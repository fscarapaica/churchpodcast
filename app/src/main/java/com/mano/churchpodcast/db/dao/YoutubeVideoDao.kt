package com.mano.churchpodcast.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mano.churchpodcast.model.YoutubeVideo

@Dao
interface YoutubeVideoDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(youtubeVideoList: List<YoutubeVideo>)

    @Query("SELECT * from youtube_video_table WHERE channelId = :channelId ORDER BY date(publishedAt) DESC")
    fun getYoutubeVideoByChannelId(channelId: String): LiveData<List<YoutubeVideo>>

    @Query("SELECT * from youtube_video_table WHERE channelId = :channelId AND date(:videoPublishedAt) > date(publishedAt) ORDER BY date(publishedAt) DESC LIMIT 10")
    fun getYoutubeVideoFromDate(channelId: String, videoPublishedAt: String): LiveData<List<YoutubeVideo>>

    @Query("SELECT * from youtube_video_table")
    fun getAllYoutubeVideo() : LiveData<List<YoutubeVideo>>

    @Query("DELETE FROM youtube_video_table WHERE id = :youtubeVideoId")
    suspend fun deleteYoutubeVideoId(youtubeVideoId: Int)

    @Query("DELETE FROM youtube_video_table")
    suspend fun deleteAll()
    
}