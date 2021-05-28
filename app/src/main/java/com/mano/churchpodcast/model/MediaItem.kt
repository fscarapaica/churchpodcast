package com.mano.churchpodcast.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "media_item_table")
data class MediaItem(val title: String? = "",
                     val description: String? = "",
                     val author: String? = "",
                     val date: String? = "",
                     val image: String? = "",
                     val link: String? = "",
                     var mediaLink: String? = ""): Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}
