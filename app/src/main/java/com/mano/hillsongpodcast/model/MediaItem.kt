package com.mano.hillsongpodcast.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "media_item_table")
data class MediaItem(val title: String? = "",
                     val description: String? = "",
                     val author: String? = "",
                     val date: String? = "",
                     val image: String? = "",
                     val link: String? = "",
                     var mediaLink: String? = "") {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @Ignore
    var track: List<Track>? = null

}
