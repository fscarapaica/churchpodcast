package com.mano.hillsongpodcast.model

data class MediaItem(val id: Int,
                     val title: String? = "",
                     val description: String? = "",
                     val image: String? = "",
                     val link: String? = "",
                     val track: List<Track>? = null)