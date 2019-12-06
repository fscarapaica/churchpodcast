package com.mano.hillsongpodcast.model

data class Track(val id: Int,
                 val format: String,
                 val isDownloadable: Boolean = true,
                 val src: String? = null)