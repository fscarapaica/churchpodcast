package com.mano.churchpodcast.model

data class Track(val id: Int,
                 val format: String,
                 val isDownloadable: Boolean = true,
                 val src: String? = null)