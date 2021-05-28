package com.mano.churchpodcast.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(val id: Int,
                    val name: String,
                    val image: String,
                    val link: String = "",
                    val jesus: String = "",
                    val channelId: String): Parcelable