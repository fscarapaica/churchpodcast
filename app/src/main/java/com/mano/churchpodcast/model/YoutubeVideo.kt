package com.mano.churchpodcast.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity(tableName = "youtube_video_table")
data class YoutubeVideo(@PrimaryKey var id: String = "",
                        var title: String = "",
                        var description: String = "",
                        var publishedAt: String = "",
                        var isLive: Boolean = false,
                        var isUpcoming: Boolean = false,
                        var channelId: String = "",
                        var channelTitle: String = "",
                        var thumbnailUrl: String = "",
                        var thumbnailRatio: Float = 0f): Parcelable