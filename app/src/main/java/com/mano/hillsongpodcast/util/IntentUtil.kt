package com.mano.hillsongpodcast.util

import android.content.Intent
import com.google.gson.Gson
import com.google.gson.GsonBuilder

const val DEFAULT_NAME = "object"

// TODO: Handle the Gson instance with Dagger
object IntentUtil {
    @Suppress("SpellCheckingInspection")
    val gson: Gson = GsonBuilder().create()
}

fun Intent.putExtraJson(name: String, src: Any) {
    putExtra(name, IntentUtil.gson.toJson(src))
}

fun Intent.putExtraJson(src: Any) {
    putExtraJson(DEFAULT_NAME, src)
}

fun <T> Intent.getJsonExtra(name: String, `class`: Class<T>): T? {
    val stringExtra = getStringExtra(name)
    if (stringExtra != null) {
        return IntentUtil.gson.fromJson<T>(stringExtra, `class`)
    }
    return null
}

fun <T> Intent.getJsonExtra(`class`: Class<T>): T? {
    return getJsonExtra(DEFAULT_NAME, `class`)
}