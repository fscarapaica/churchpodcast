package com.mano.churchpodcast.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder

// TODO: Handle the Gson instance with Hilt
object GsonUtil {
    @Suppress("SpellCheckingInspection")
    val gson: Gson = GsonBuilder().create()

    fun toJson(src: Any?) : String = gson.toJson(src)

    fun <T> fromJson(stringExtra: String, `class`: Class<T>): T = gson.fromJson(
        stringExtra,
        `class`
    )

}
