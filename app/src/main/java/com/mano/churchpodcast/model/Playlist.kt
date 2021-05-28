package com.mano.churchpodcast.model

data class Playlist(val id: String = "",
                    val name: String = "",
                    val imageUrl: String = "",
                    val season: Int = -1,
                    val comingSoon: Boolean = false) {

    var mediaItems: List<MediaItem> = emptyList()

}