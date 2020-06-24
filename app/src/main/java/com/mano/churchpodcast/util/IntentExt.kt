package com.mano.churchpodcast.util

import android.content.Intent

private const val DEFAULT_NAME = "object"

fun Intent.putExtraJson(name: String, src: Any) {
    putExtra(name, GsonUtil.toJson(src))
}

fun Intent.putExtraJson(src: Any) {
    putExtraJson(DEFAULT_NAME, src)
}

fun <T> Intent.getJsonExtra(name: String, `class`: Class<T>): T? = getStringExtra(name)?.let {
        GsonUtil.fromJson(it, `class`)
}

fun <T> Intent.getJsonExtra(`class`: Class<T>): T? = getJsonExtra(DEFAULT_NAME, `class`)
