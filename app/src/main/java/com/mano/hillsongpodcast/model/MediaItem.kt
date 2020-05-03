package com.mano.hillsongpodcast.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "media_item_table")
data class MediaItem(@PrimaryKey(autoGenerate = true) val id: Int,
                     val title: String? = "",
                     val description: String? = "",
                     val author: String? = "",
                     val date: String? = "",
                     val image: String? = "",
                     val link: String? = "",
                     var mediaLink: String? = "") {

    @Ignore
    var track: List<Track>? = null
}
