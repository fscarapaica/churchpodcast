package com.mano.churchpodcast.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Location(val id: Int,
                    val name: String,
                    val image: String,
                    val link: String = "",
                    val jesus: String = "",
                    val channelId: String): Parcelable